package application.commands;

public interface Command {

    String id();
    void execute();

    default SelectionPolicy selectionPolicy() {
        return SelectionPolicy.KEEP;
    }
}
