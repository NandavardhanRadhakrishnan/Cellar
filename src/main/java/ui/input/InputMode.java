package ui.input;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@Getter
@RequiredArgsConstructor
public enum InputMode {
    NAVIGATE("NAV", new Color(115, 229, 115)),
    EDIT("EDIT", new Color(255, 124, 62)),
    SELECT("SEL", new Color(100, 149, 237));

    private final String label;
    private final Color color;

}
