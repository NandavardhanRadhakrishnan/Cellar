package application.commands;

import lombok.Data;
import ui.ThemeManager;

@Data
public class ToggleDarkModeCommand implements Command {

  @Override
  public String id() {
    return "toggle_dark_mode";
  }

  @Override
  public void execute() {
    ThemeManager.toggleTheme();
  }

  @Override
  public SelectionPolicy selectionPolicy() {
    return SelectionPolicy.KEEP;
  }
}
