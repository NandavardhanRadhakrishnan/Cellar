package ui.clipboard;

import core.clipboard.Clipboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class SystemClipboard implements Clipboard {

    private final java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    @Override
    public void set(String text) {
        clipboard.setContents(new StringSelection(text), null);
    }

    @Override
    public String get() {
        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return "";
        }
    }
}
