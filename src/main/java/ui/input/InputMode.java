package ui.input;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InputMode {
    NAVIGATE("NAV", "\033[48;2;115;229;115m\033[38;2;0;0;0m"),
    EDIT("EDIT", "\033[48;2;255;124;62m\033[38;2;0;0;0m"),
    SELECT("SEL", "\033[48;2;100;149;237m\033[38;2;255;255;255m");

    private final String label;
    private final String color;

}
