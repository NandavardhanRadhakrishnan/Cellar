package application.commands;

import application.TargetResolver;
import core.clipboard.Clipboard;
import core.grid.CellAddress;
import core.grid.Grid;
import lombok.Data;



@Data
public class CopyCellsCommand implements Command {

    private final TargetResolver targetResolver;
    private final Grid grid;
    private final Clipboard clipboard;

    @Override
    public String id() {
        return "copy";
    }


    @Override
    public void execute() {
        StringBuilder sb = new StringBuilder();

        for (var row : targetResolver.resolveRows()) {
            boolean first = true;

            for (CellAddress addr : row) {
                if (!first) sb.append(",");
                first = false;

                sb.append(grid.getValue(addr).display());
            }
            sb.append("\n");
        }

        if (!sb.isEmpty()) sb.setLength(sb.length() - 1);
        clipboard.set(sb.toString());
    }

}
