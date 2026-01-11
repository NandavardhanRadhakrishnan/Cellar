package ui.input;

import lombok.Value;

import java.awt.event.KeyEvent;

@Value
public class KeyStroke {
    int keyCode;
    boolean ctrl;
    boolean shift;
    boolean alt;

    public static KeyStroke from(KeyEvent e){
        return new KeyStroke(
                e.getKeyCode(),
                e.isControlDown(),
                e.isShiftDown(),
                e.isAltDown()
        );
    }

    public KeyStroke(int keyCode) {
        this.keyCode = keyCode;
        this.ctrl = false;
        this.shift = false;
        this.alt = false;
    }

    public KeyStroke(int keyCode, boolean ctrl, boolean shift, boolean alt) {
        this.keyCode = keyCode;
        this.ctrl = ctrl;
        this.shift = shift;
        this.alt = alt;
    }
}
