package ui;

import java.awt.*;

public class ThemeManager {

  private static boolean isDarkMode = false;

  // Light mode colors
  private static final Color LIGHT_BG = Color.WHITE;
  private static final Color LIGHT_TEXT = Color.BLACK;
  private static final Color LIGHT_GRID_BORDER = Color.GRAY;
  private static final Color LIGHT_CURSOR_BORDER = new Color(255, 102, 0);
  private static final Color LIGHT_SELECTION_BG = new Color(128, 176, 255);
  private static final Color LIGHT_SELECTION_TEXT = Color.WHITE;
  private static final Color LIGHT_PANEL_BG = Color.WHITE;
  private static final Color LIGHT_STATUS_BAR_BG = new Color(235, 235, 235);

  // Dark mode colors
  private static final Color DARK_BG = new Color(30, 30, 30);
  private static final Color DARK_TEXT = new Color(220, 220, 220);
  private static final Color DARK_GRID_BORDER = new Color(80, 80, 80);
  private static final Color DARK_CURSOR_BORDER = new Color(255, 140, 0);
  private static final Color DARK_SELECTION_BG = new Color(70, 130, 180);
  private static final Color DARK_SELECTION_TEXT = new Color(220, 220, 220);
  private static final Color DARK_PANEL_BG = new Color(25, 25, 25);
  private static final Color DARK_STATUS_BAR_BG = new Color(40, 40, 40);

  public static void toggleTheme() {
    isDarkMode = !isDarkMode;
  }

  public static boolean isDarkMode() {
    return isDarkMode;
  }

  public static Color getCellBackground(
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

  public static Color getCellText(boolean isSelected) {
    if (isDarkMode) {
      return isSelected ? DARK_SELECTION_TEXT : DARK_TEXT;
    } else {
      return isSelected ? LIGHT_SELECTION_TEXT : LIGHT_TEXT;
    }
  }

  public static Color getGridBorder() {
    return isDarkMode ? DARK_GRID_BORDER : LIGHT_GRID_BORDER;
  }

  public static Color getCursorBorder() {
    return isDarkMode ? DARK_CURSOR_BORDER : LIGHT_CURSOR_BORDER;
  }

  public static Color getPanelBackground() {
    return isDarkMode ? DARK_PANEL_BG : LIGHT_PANEL_BG;
  }

  public static Color getStatusBarBackground() {
    return isDarkMode ? DARK_STATUS_BAR_BG : LIGHT_STATUS_BAR_BG;
  }

  public static Color getStatusBarText() {
    return isDarkMode ? DARK_TEXT : LIGHT_TEXT;
  }
}
