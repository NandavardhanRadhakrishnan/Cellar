package ui.input;

import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;

@FunctionalInterface
public interface InputAction {
    void run(KeyPressMessage e);
}
