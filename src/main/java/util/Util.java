package util;

import core.grid.CellAddress;

public class Util {

    public static String intToColumnLabel(int col) {
        StringBuilder label = new StringBuilder();
        while (col >= 0) {
            label.insert(0, (char) ('A' + (col % 26)));
            col = (col / 26) - 1;
        }
        return label.toString();
    }

    public static String intToRowLabel(int row) {
        return String.valueOf(row + 1);
    }

    public static String cellAddressToLabel(int row, int col) {
        return intToColumnLabel(col) + intToRowLabel(row);
    }

    public static String cellAddressToLabel(CellAddress address) {
        return cellAddressToLabel(address.getRow(), address.getCol());
    }

    public static int columnLabelToInt(String label) {
        int col = 0;
        for (int i = 0; i < label.length(); i++) {
            col = col * 26 + (label.charAt(i) - 'A' + 1);
        }
        return col - 1;
    }

    public static int rowLabelToInt(String label) {
        return Integer.parseInt(label) - 1;
    }

    public static CellAddress labelToCellAddress(String label) {
        int i = 0;

        while (i < label.length() && Character.isLetter(label.charAt(i))) {
            i++;
        }

        if (i == 0 || i == label.length()) {
            throw new IllegalArgumentException("Invalid cell label: " + label);
        }

        String colPart = label.substring(0, i).toUpperCase();
        String rowPart = label.substring(i);

        int col = columnLabelToInt(colPart);
        int row = rowLabelToInt(rowPart);

        return new CellAddress(row, col);
    }
}
