package application;

import application.commands.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommandRegistry {

    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandRegistry(List<Command> commandList){
        for (Command command : commandList) {
            commandMap.put(command.id(), command);
        }
    }

    public Command command(String id){
        return commandMap.get(id);
    }
}
