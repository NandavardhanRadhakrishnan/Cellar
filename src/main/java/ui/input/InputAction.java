package ui.input;

import java.awt.event.KeyEvent;

@FunctionalInterface
public interface InputAction {
    void run(KeyEvent e);
}
