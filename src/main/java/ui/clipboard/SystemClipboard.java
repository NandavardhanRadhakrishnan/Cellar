package ui.clipboard;

import core.clipboard.Clipboard;

public class SystemClipboard implements Clipboard {

    private String clipboard = "";

    @Override
    public void set(String text) {
        clipboard = text;
    }

    @Override
    public String get() {
        return clipboard;
    }
}
