package command;

public interface Command {
    void undo();

    void redo();

    void execute();

    boolean isExecuted();
}