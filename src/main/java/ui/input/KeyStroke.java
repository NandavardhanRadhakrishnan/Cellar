package ui.input;

import lombok.Value;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;

@Value
public class KeyStroke {
    String key;
    boolean alt;

    public static KeyStroke from(KeyPressMessage e){
        return new KeyStroke(e.key().toLowerCase(), e.alt());
    }

    public KeyStroke(String key) {
        this.key = key;
        this.alt = false;
    }

    public KeyStroke(String key, boolean alt) {
        this.key = key;
        this.alt = alt;
    }
}
