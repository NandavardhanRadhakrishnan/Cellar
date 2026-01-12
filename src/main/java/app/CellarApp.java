package app;

import application.CommandRegistry;
import application.TargetResolver;
import application.commands.ClearCellsCommand;
import application.commands.CopyCellsCommand;
import core.clipboard.Clipboard;
import core.grid.Grid;
import core.grid.selection.SelectionManager;
import ui.*;
import ui.CellEditor;
import ui.clipboard.SystemClipboard;
import ui.input.InputController;

import javax.swing.*;
import java.util.List;

public class CellarApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // --- core ---
            Grid grid = new Grid(20, 10);
            SelectionManager selectionManager = new SelectionManager();

            // --- ui/application state ---
            Cursor cursor = new Cursor();
            CellEditor editor = new CellEditor();

            // --- application ---
            TargetResolver targetResolver = new TargetResolver(grid, cursor, selectionManager);
            Clipboard clipboard = new SystemClipboard();

            CommandRegistry commandRegistry = new CommandRegistry(List.of(
                    new ClearCellsCommand(targetResolver),
                    new CopyCellsCommand(targetResolver, grid, clipboard)
            ));

            InputController input =
                    new InputController(
                            grid,
                            cursor,
                            editor,
                            selectionManager,
                            commandRegistry
                    );


            // --- ui ---
            SpreadsheetPanel panel =
                    new SpreadsheetPanel(
                            grid,
                            cursor,
                            editor,
                            selectionManager,
                            input
                    );

            JFrame frame = new JFrame("Cellar");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null);
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
