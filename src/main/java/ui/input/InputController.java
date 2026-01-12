package ui.input;

import application.CommandRegistry;
import application.commands.Command;
import core.grid.CellAddress;
import core.grid.Grid;
import core.grid.selection.SelectionManager;
import core.value.NumberValue;
import core.value.StringValue;
import core.value.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ui.CellEditor;
import ui.Cursor;

import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public final class InputController {

    InputMode mode = InputMode.NAVIGATE;

    private final Grid grid;
    private final Cursor cursor;
    private final CellEditor editor;
    private final SelectionManager selectionManager;
    private final CommandRegistry commandRegistry;


    final Map<InputMode, Map<KeyStroke, InputAction>> keymap =
            new EnumMap<InputMode, Map<KeyStroke, InputAction>>(InputMode.class);

    {
        initNavigateMode();
        initSelectMode();
        initEditMode();
    }


    public void handleKey(KeyEvent e) {
        Map<KeyStroke, InputAction> actions =
                keymap.getOrDefault(mode, Map.of());

        InputAction action = actions.get(KeyStroke.from(e));
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
        Map<KeyStroke, InputAction> nav = new HashMap<>();

        nav.put(new KeyStroke(KeyEvent.VK_W), e -> moveCursor(-1, 0));
        nav.put(new KeyStroke(KeyEvent.VK_S), e -> moveCursor(1, 0));
        nav.put(new KeyStroke(KeyEvent.VK_A), e -> moveCursor(0, -1));
        nav.put(new KeyStroke(KeyEvent.VK_D), e -> moveCursor(0, 1));

        nav.put(new KeyStroke(KeyEvent.VK_SPACE), e -> {
            mode = InputMode.SELECT;
            selectionManager.startSelection(
                    new CellAddress(cursor.row, cursor.col)
            );
        });

        nav.put(new KeyStroke(KeyEvent.VK_ENTER), e -> enterEditMode());

//        Commands
        nav.put(new KeyStroke(KeyEvent.VK_BACK_SPACE), e -> runCommand(commandRegistry.command("clear_cells")));
        nav.put(new KeyStroke(KeyEvent.VK_C,true,false,false), e -> runCommand(commandRegistry.command("copy")));

        keymap.put(InputMode.NAVIGATE, nav);
    }

    void initSelectMode() {
        Map<KeyStroke, InputAction> select = new HashMap<>();

        select.put(new KeyStroke(KeyEvent.VK_W), e -> moveCursor(-1, 0));
        select.put(new KeyStroke(KeyEvent.VK_S), e -> moveCursor(1, 0));
        select.put(new KeyStroke(KeyEvent.VK_A), e -> moveCursor(0, -1));
        select.put(new KeyStroke(KeyEvent.VK_D), e -> moveCursor(0, 1));

        select.put(new KeyStroke(KeyEvent.VK_SPACE), e -> mode = InputMode.NAVIGATE);
        select.put(new KeyStroke(KeyEvent.VK_ENTER), e -> enterEditMode());

//        Commands
        select.put(new KeyStroke(KeyEvent.VK_BACK_SPACE), e -> runCommand(commandRegistry.command("clear_cells")));

        keymap.put(InputMode.SELECT, select);
    }

    void initEditMode() {
        Map<KeyStroke, InputAction> edit = new HashMap<>();

        edit.put(new KeyStroke(KeyEvent.VK_ENTER), e -> {
            commitEditorValue();
            mode = InputMode.NAVIGATE;
        });

        edit.put(new KeyStroke(KeyEvent.VK_BACK_SPACE), e -> editor.backspace());
        edit.put(new KeyStroke(KeyEvent.VK_LEFT), e -> editor.moveLeft());
        edit.put(new KeyStroke(KeyEvent.VK_RIGHT), e -> editor.moveRight());
        edit.put(new KeyStroke(KeyEvent.VK_SPACE), e -> editor.append(' '));

        keymap.put(InputMode.EDIT, edit);
    }

    void runCommand(Command command) {
        command.execute();

        switch (command.selectionPolicy()) {
            case KEEP -> {
            }
            case CLEAR -> selectionManager.clear();
            case COLLAPSE -> selectionManager.startSelection(new CellAddress(cursor.row, cursor.col));
        }
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
