package org.lld.filesystem;

import org.lld.filesystem.shell.FileSystemShell;

public final class FileSystemDemo {

    public static void main(String[] args) {
        FileSystemFacade fileSystem = FileSystemFacade.getInstance();
        FileSystemShell shell = new FileSystemShell(fileSystem);

        String[] commands = {
                "pwd",
                "mkdir home",
                "mkdir home/docs",
                "cd home",
                "pwd",
                "touch readme.txt",
                "touch docs/report.txt",
                "ls",
                "ls docs",
                "cd ..",
                "pwd",
                "ls home",
                "cd .",
                "ls",
                "mkdir ab",
                "ls",
                "pwd"
        };

        System.out.println("=== In-memory File System Demo ===");
        for (String command : commands) {
            System.out.println("$ " + command);
            shell.execute(command);
            System.out.println();
        }
    }
}
