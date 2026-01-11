package application.commands;

import application.TargetResolver;
import core.grid.Cell;
import lombok.Data;

@Data
public class ClearCellsCommand implements Command {

    private final TargetResolver targetResolver;

    @Override
    public String id() {
        return "clear_cells";
    }

    @Override
    public void execute() {
        targetResolver.forEachCell(Cell::clear);
    }

    @Override
    public SelectionPolicy selectionPolicy(){
        return SelectionPolicy.CLEAR;
    }
}
