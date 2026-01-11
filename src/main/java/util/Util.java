package util;

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

}
