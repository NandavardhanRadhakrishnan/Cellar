package ui;

import ui.input.InputMode;

public class StatusBar {

  public String render(int width, InputMode mode, String editorText) {
    String modeStr = String.format(" %-4s ", mode.getLabel());
    String modeStyled = mode.getColor() + modeStr + ThemeManager.getReset();

    String textStr = " " + editorText;
    int visibleTextLen = textStr.length();
    int modeLen = modeStr.length();

    int remaining = width - modeLen - visibleTextLen;
    if (remaining < 0) {
        remaining = 0;
        int maxTextLen = width - modeLen;
        if (maxTextLen > 0) {
            textStr = textStr.substring(0, maxTextLen);
        } else {
            textStr = "";
        }
    }

    String padding = " ".repeat(remaining);

    return modeStyled + ThemeManager.getStatusBarBackground() + ThemeManager.getStatusBarText() + textStr + padding + ThemeManager.getReset();
  }
}
