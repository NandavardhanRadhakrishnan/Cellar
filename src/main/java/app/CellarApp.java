package app;

import application.CommandRegistry;
import application.TargetResolver;
import application.commands.ClearCellsCommand;
import application.commands.CopyCellsCommand;
import application.commands.ToggleDarkModeCommand;
import core.clipboard.Clipboard;
import core.formula.FormulaEngine;
import core.grid.Grid;
import core.grid.selection.SelectionManager;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import java.util.List;
import ui.*;
import ui.clipboard.SystemClipboard;
import ui.input.InputController;

public class CellarApp {

  public static void main(String[] args) {
      // --- core ---
      Grid grid = new Grid(20, 10);
      SelectionManager selectionManager = new SelectionManager();
      FormulaEngine formulaEngine = new FormulaEngine();

      // --- ui/application state ---
      Cursor cursor = new Cursor();
      CellEditor editor = new CellEditor();

      // --- application ---
      TargetResolver targetResolver = new TargetResolver(
        grid,
        cursor,
        selectionManager
      );
      Clipboard clipboard = new SystemClipboard();

      CommandRegistry commandRegistry = new CommandRegistry(
        List.of(
          new ClearCellsCommand(targetResolver),
          new CopyCellsCommand(targetResolver, grid, clipboard),
          new ToggleDarkModeCommand()
        )
      );

      InputController input = new InputController(
        grid,
        cursor,
        editor,
        selectionManager,
        commandRegistry,
        formulaEngine
      );

      // --- ui ---
      SpreadsheetPanel panel = new SpreadsheetPanel(
        grid,
        cursor,
        editor,
        selectionManager,
        input,
        formulaEngine
      );

      new Program(panel).run();
  }
}
