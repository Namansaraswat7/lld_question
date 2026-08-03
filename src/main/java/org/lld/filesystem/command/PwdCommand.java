package org.lld.filesystem.command;

import org.lld.filesystem.FileSystemFacade;

public class PwdCommand implements Command {
    private final FileSystemFacade fs;

    public PwdCommand(FileSystemFacade fs) {
        this.fs = fs;
    }

    @Override
    public void execute() {
        System.out.println(fs.getWorkingDirectory());
    }
}