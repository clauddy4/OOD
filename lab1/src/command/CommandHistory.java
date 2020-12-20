package command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class CommandHistory {
    private Deque<Command> history = new ArrayDeque<>();

    public void push(Command command) {
        history.push(command);
    }

    public Command undoTop() {
        return history
                .stream()
                .filter(Command::isExecuted)
                .findFirst()
                .orElseThrow();
    }

    public Command redoTop() {
        var reversed = new ArrayList<Command>();
        history.descendingIterator().forEachRemaining(reversed::add);
        return reversed
                .stream()
                .filter(command -> !command.isExecuted())
                .findFirst()
                .orElseThrow();
    }

    public boolean canUndo() {
        return history.stream().anyMatch(Command::isExecuted);
    }

    public boolean canRedo() {
        return history.stream().anyMatch(command -> !command.isExecuted());
    }
}