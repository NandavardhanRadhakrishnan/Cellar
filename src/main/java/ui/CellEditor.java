package ui;

public class CellEditor {

    private final StringBuilder buf = new StringBuilder();
    private int caret = 0; // insertion point

    public void clear() {
        buf.setLength(0);
        caret = 0;
    }

    public void append(String s) {
        buf.setLength(0);
        buf.append(s);
        caret = buf.length();
    }

    public void append(char c) {
        buf.insert(caret, c);
        caret++;
    }

    public void backspace() {
        if (caret > 0) {
            buf.deleteCharAt(caret - 1);
            caret--;
        }
    }

    public void moveLeft() {
        caret = Math.max(0, caret - 1);
    }

    public void moveRight() {
        caret = Math.min(buf.length(), caret + 1);
    }

    public String value() {
        return buf.toString();
    }

    public int caret() {
        return caret;
    }

    public String valueWithCursor() {
        return buf.substring(0, caret) + "|" + buf.substring(caret);
    }
}
