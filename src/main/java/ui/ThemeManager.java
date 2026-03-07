package ui;

public class ThemeManager {

  private static boolean isDarkMode = false;

  // Light mode colors
  private static final String LIGHT_BG = "\033[0m"; // default background
  private static final String LIGHT_TEXT = "\033[38;2;0;0;0m";
  private static final String LIGHT_GRID_BORDER = "\033[38;2;128;128;128m";
  private static final String LIGHT_CURSOR_BORDER = "\033[38;2;255;102;0m";
  private static final String LIGHT_SELECTION_BG = "\033[48;2;128;176;255m";
  private static final String LIGHT_SELECTION_TEXT = "\033[38;2;255;255;255m";
  private static final String LIGHT_PANEL_BG = "\033[0m";
  private static final String LIGHT_STATUS_BAR_BG = "\033[48;2;235;235;235m";

  // Dark mode colors
  private static final String DARK_BG = "\033[48;2;30;30;30m";
  private static final String DARK_TEXT = "\033[38;2;220;220;220m";
  private static final String DARK_GRID_BORDER = "\033[38;2;80;80;80m";
  private static final String DARK_CURSOR_BORDER = "\033[38;2;255;140;0m";
  private static final String DARK_SELECTION_BG = "\033[48;2;70;130;180m";
  private static final String DARK_SELECTION_TEXT = "\033[38;2;220;220;220m";
  private static final String DARK_PANEL_BG = "\033[48;2;25;25;25m";
  private static final String DARK_STATUS_BAR_BG = "\033[48;2;40;40;40m";

  public static void toggleTheme() {
    isDarkMode = !isDarkMode;
  }

  public static boolean isDarkMode() {
    return isDarkMode;
  }

  public static String getCellBackground(
    boolean isCursorCell,
    boolean isSelected
  ) {
    if (isDarkMode) {
      if (isCursorCell) return DARK_BG;
      if (isSelected) return DARK_SELECTION_BG;
      return DARK_BG;
    } else {
      if (isCursorCell) return LIGHT_BG;
      if (isSelected) return LIGHT_SELECTION_BG;
      return LIGHT_BG;
    }
  }

  public static String getCellText(boolean isSelected) {
    if (isDarkMode) {
      return isSelected ? DARK_SELECTION_TEXT : DARK_TEXT;
    } else {
      return isSelected ? LIGHT_SELECTION_TEXT : LIGHT_TEXT;
    }
  }

  public static String getGridBorder() {
    return isDarkMode ? DARK_GRID_BORDER : LIGHT_GRID_BORDER;
  }

  public static String getCursorBorder() {
    return isDarkMode ? DARK_CURSOR_BORDER : LIGHT_CURSOR_BORDER;
  }

  public static String getPanelBackground() {
    return isDarkMode ? DARK_PANEL_BG : LIGHT_PANEL_BG;
  }

  public static String getStatusBarBackground() {
    return isDarkMode ? DARK_STATUS_BAR_BG : LIGHT_STATUS_BAR_BG;
  }

  public static String getStatusBarText() {
    return isDarkMode ? DARK_TEXT : LIGHT_TEXT;
  }
  
  public static String getReset() {
    return "\033[0m";
  }
}
