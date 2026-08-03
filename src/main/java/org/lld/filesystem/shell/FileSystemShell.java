package org.lld.filesystem.shell;

import org.lld.filesystem.FileSystemFacade;
import org.lld.filesystem.command.CdCommand;
import org.lld.filesystem.command.Command;
import org.lld.filesystem.command.LsCommand;
import org.lld.filesystem.command.MkdirCommand;
import org.lld.filesystem.command.PwdCommand;
import org.lld.filesystem.command.TouchCommand;

import java.util.Objects;

/**
 * Shell + command factory: parses input and invokes the matching {@link Command}.
 */
public final class FileSystemShell {

    private final FileSystemFacade fileSystem;

    public FileSystemShell(FileSystemFacade fileSystem) {
        this.fileSystem = Objects.requireNonNull(fileSystem, "fileSystem");
    }

    public void execute(String input) {
        Command command = createCommand(input);
        if (command == null) {
            System.out.println("Unknown command. Supported: pwd, cd, mkdir, touch, ls");
            return;
        }
        command.execute();
    }

    public Command createCommand(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }

        String trimmed = input.trim();
        int spaceIndex = trimmed.indexOf(' ');
        String name = spaceIndex == -1 ? trimmed : trimmed.substring(0, spaceIndex);
        String argument = spaceIndex == -1 ? "" : trimmed.substring(spaceIndex + 1).trim();

        return switch (name) {
            case "pwd" -> new PwdCommand(fileSystem);
            case "cd" -> argument.isEmpty() ? null : new CdCommand(fileSystem, argument);
            case "mkdir" -> argument.isEmpty() ? null : new MkdirCommand(fileSystem, argument);
            case "touch" -> argument.isEmpty() ? null : new TouchCommand(fileSystem, argument);
            case "ls" -> new LsCommand(fileSystem, argument);
            default -> null;
        };
    }
}
