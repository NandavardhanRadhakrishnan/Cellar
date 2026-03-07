package ui.input;

import application.CommandRegistry;
import application.commands.Command;
import core.formula.FormulaEngine;
import core.grid.CellAddress;
import core.grid.Grid;
import core.grid.selection.SelectionManager;
import core.value.Value;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ui.CellEditor;
import ui.Cursor;

@Getter
@RequiredArgsConstructor
public final class InputController {

  InputMode mode = InputMode.NAVIGATE;

  private final Grid grid;
  private final Cursor cursor;
  private final CellEditor editor;
  private final SelectionManager selectionManager;
  private final CommandRegistry commandRegistry;
  private final FormulaEngine formulaEngine;

  final Map<InputMode, Map<KeyStroke, InputAction>> keymap = new EnumMap<InputMode, Map<KeyStroke, InputAction>>(
    InputMode.class
  );

  {
    initNavigateMode();
    initSelectMode();
    initEditMode();
  }

  public void handleKey(KeyPressMessage e) {
    Map<KeyStroke, InputAction> actions = keymap.getOrDefault(mode, Map.of());

    InputAction action = actions.get(KeyStroke.from(e));
    if (action != null) {
      action.run(e);
      return;
    }

    // A fallback if key is capitalised but we registered lowercase
    String lowerKey = e.key().toLowerCase();
    KeyStroke fallbackStroke = new KeyStroke(lowerKey, e.alt());
    InputAction fallbackAction = actions.get(fallbackStroke);
    if (fallbackAction != null) {
        fallbackAction.run(e);
        return;
    }

    // default character handling
    if (mode == InputMode.EDIT) {
      String key = e.key();
      if (key.length() == 1 && !e.alt()) {
        editor.append(key.charAt(0));
      }
    }
  }

  void initNavigateMode() {
    Map<KeyStroke, InputAction> nav = new HashMap<>();

    nav.put(new KeyStroke("w"), e -> moveCursor(-1, 0));
    nav.put(new KeyStroke("s"), e -> moveCursor(1, 0));
    nav.put(new KeyStroke("a"), e -> moveCursor(0, -1));
    nav.put(new KeyStroke("d"), e -> moveCursor(0, 1));
    // Up/down/left/right defaults
    nav.put(new KeyStroke("up"), e -> moveCursor(-1, 0));
    nav.put(new KeyStroke("down"), e -> moveCursor(1, 0));
    nav.put(new KeyStroke("left"), e -> moveCursor(0, -1));
    nav.put(new KeyStroke("right"), e -> moveCursor(0, 1));

    nav.put(
      new KeyStroke(" "),
      e -> {
        mode = InputMode.SELECT;
        selectionManager.startSelection(
          new CellAddress(cursor.row, cursor.col)
        );
      }
    );

    nav.put(new KeyStroke("enter"), e -> enterEditMode());

    // Commands
    nav.put(
      new KeyStroke("ctrl+h"),
      e -> runCommand(commandRegistry.command("clear_cells"))
    );
    // Ctrl+C
    nav.put(
      new KeyStroke("ctrl+c"),
      e -> runCommand(commandRegistry.command("copy"))
    );
    // Ctrl+Shift+T
    nav.put(
      new KeyStroke("ctrl+t"),
      e -> runCommand(commandRegistry.command("toggle_dark_mode"))
    );

    keymap.put(InputMode.NAVIGATE, nav);
  }

  void initSelectMode() {
    Map<KeyStroke, InputAction> select = new HashMap<>();

    select.put(new KeyStroke("w"), e -> moveCursor(-1, 0));
    select.put(new KeyStroke("s"), e -> moveCursor(1, 0));
    select.put(new KeyStroke("a"), e -> moveCursor(0, -1));
    select.put(new KeyStroke("d"), e -> moveCursor(0, 1));
    select.put(new KeyStroke("up"), e -> moveCursor(-1, 0));
    select.put(new KeyStroke("down"), e -> moveCursor(1, 0));
    select.put(new KeyStroke("left"), e -> moveCursor(0, -1));
    select.put(new KeyStroke("right"), e -> moveCursor(0, 1));

    select.put(
      new KeyStroke(" "),
      e -> mode = InputMode.NAVIGATE
    );
    select.put(new KeyStroke("enter"), e -> enterEditMode());

    // Commands
    select.put(
      new KeyStroke("ctrl+h"),
      e -> runCommand(commandRegistry.command("clear_cells"))
    );
    // Same copy/theme keys
    select.put(
      new KeyStroke("ctrl+c"),
      e -> runCommand(commandRegistry.command("copy"))
    );
    select.put(
      new KeyStroke("ctrl+t"),
      e -> runCommand(commandRegistry.command("toggle_dark_mode"))
    );

    keymap.put(InputMode.SELECT, select);
  }

  void initEditMode() {
    Map<KeyStroke, InputAction> edit = new HashMap<>();

    edit.put(
      new KeyStroke("enter"),
      e -> {
        commitEditorValue();
        mode = InputMode.NAVIGATE;
      }
    );

    edit.put(new KeyStroke("ctrl+h"), e -> editor.backspace());
    edit.put(new KeyStroke("left"), e -> editor.moveLeft());
    edit.put(new KeyStroke("right"), e -> editor.moveRight());
    edit.put(new KeyStroke(" "), e -> editor.append(' '));

    keymap.put(InputMode.EDIT, edit);
  }

  void runCommand(Command command) {
    command.execute();

    switch (command.selectionPolicy()) {
      case KEEP -> {}
      case CLEAR -> selectionManager.clear();
      case COLLAPSE -> selectionManager.startSelection(
        new CellAddress(cursor.row, cursor.col)
      );
    }
  }

  void moveCursor(int dRow, int dCol) {
    cursor.row += dRow;
    cursor.col += dCol;

    if (mode == InputMode.SELECT) {
      selectionManager.update(new CellAddress(cursor.row, cursor.col));
    }
  }

  void enterEditMode() {
    mode = InputMode.EDIT;
    editor.clear();

    CellAddress addr = new CellAddress(cursor.row, cursor.col);
    var cell = grid.getCell(addr);

    if (cell == null) return;

    Value raw = cell.getRaw();
    if (raw == null || raw.isEmpty()) return;

    // IMPORTANT: use RAW, not computed/display
    editor.append(raw.display());
  }

  void commitEditorValue() {
    String raw = editor.value();
    CellAddress cellAddress = new CellAddress(cursor.row, cursor.col);

    if (raw.isBlank()) {
      grid.getCell(cursor.row, cursor.col).clear();
      grid.recalculateAll(formulaEngine.evaluator());
      return;
    }

    Value parsed = formulaEngine.parseValue(raw);
    grid.setCell(cellAddress, parsed);
    grid.recalculateAll(formulaEngine.evaluator());
  }
}
