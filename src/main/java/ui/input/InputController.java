package ui.input;

import core.grid.CellAddress;
import core.grid.Grid;
import core.grid.selection.SelectionManager;
import core.value.NumberValue;
import core.value.StringValue;
import core.value.Value;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ui.CellEditor;
import ui.Cursor;

import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class InputController {

    InputMode mode = InputMode.NAVIGATE;

    @NonNull Grid grid;
    @NonNull
    Cursor cursor;
    @NonNull
    CellEditor editor;
    @NonNull SelectionManager selectionManager;

    final Map<InputMode, Map<Integer, InputAction>> keymap =
            new EnumMap<>(InputMode.class);

    {
        initNavigateMode();
        initSelectMode();
        initEditMode();
    }


    public void handleKey(KeyEvent e) {
        Map<Integer, InputAction> actions =
                keymap.getOrDefault(mode, Map.of());

        InputAction action = actions.get(e.getKeyCode());
        if (action != null) {
            action.run(e);
            return;
        }

        // default character handling
        if (mode == InputMode.EDIT) {
            char ch = e.getKeyChar();
            if (!Character.isISOControl(ch)) {
                editor.append(ch);
            }
        }
    }


    void initNavigateMode() {
        Map<Integer, InputAction> nav = new HashMap<>();

        nav.put(KeyEvent.VK_W, e -> moveCursor(-1, 0));
        nav.put(KeyEvent.VK_S, e -> moveCursor(1, 0));
        nav.put(KeyEvent.VK_A, e -> moveCursor(0, -1));
        nav.put(KeyEvent.VK_D, e -> moveCursor(0, 1));

        nav.put(KeyEvent.VK_SPACE, e -> {
            mode = InputMode.SELECT;
            selectionManager.startSelection(
                    new CellAddress(cursor.row, cursor.col)
            );
        });

        nav.put(KeyEvent.VK_ENTER, e -> enterEditMode());

        keymap.put(InputMode.NAVIGATE, nav);
    }

    void initSelectMode() {
        Map<Integer, InputAction> select = new HashMap<>();

        select.put(KeyEvent.VK_W, e -> moveCursor(-1, 0));
        select.put(KeyEvent.VK_S, e -> moveCursor(1, 0));
        select.put(KeyEvent.VK_A, e -> moveCursor(0, -1));
        select.put(KeyEvent.VK_D, e -> moveCursor(0, 1));

        select.put(KeyEvent.VK_SPACE, e -> mode = InputMode.NAVIGATE);
        select.put(KeyEvent.VK_ENTER, e -> enterEditMode());

        keymap.put(InputMode.SELECT, select);
    }

    void initEditMode() {
        Map<Integer, InputAction> edit = new HashMap<>();

        edit.put(KeyEvent.VK_ENTER, e -> {
            commitEditorValue();
            mode = InputMode.NAVIGATE;
        });

        edit.put(KeyEvent.VK_BACK_SPACE, e -> editor.backspace());
        edit.put(KeyEvent.VK_LEFT, e -> editor.moveLeft());
        edit.put(KeyEvent.VK_RIGHT, e -> editor.moveRight());
        edit.put(KeyEvent.VK_SPACE, e -> editor.append(' '));

        keymap.put(InputMode.EDIT, edit);
    }


    void moveCursor(int dRow, int dCol) {
        cursor.row += dRow;
        cursor.col += dCol;

        if (mode == InputMode.SELECT) {
            selectionManager.update(
                    new CellAddress(cursor.row, cursor.col)
            );
        }
    }

    void enterEditMode() {
        mode = InputMode.EDIT;
        editor.clear();

        Value value = grid.getValue(
                new CellAddress(cursor.row, cursor.col)
        );

        if (!value.isEmpty()) {
            editor.append(value.display());
        }
    }

    void commitEditorValue() {
        String raw = editor.value();

        if (raw.isBlank()) {
            grid.getCell(cursor.row, cursor.col).clear();
            return;
        }

        try {
            double n = Double.parseDouble(raw);
            grid.setCell(cursor.row, cursor.col, new NumberValue(n));
        } catch (NumberFormatException e) {
            grid.setCell(cursor.row, cursor.col, new StringValue(raw));
        }
    }
}
