package org.lld.filesystem.command;

import org.lld.filesystem.FileSystemFacade;

public class MkdirCommand implements Command {
    private final FileSystemFacade fs;
    private final String path;

    public MkdirCommand(FileSystemFacade fs, String path) {
        this.fs = fs;
        this.path = path;
    }

    @Override
    public void execute() { fs.createDirectory(path); }
}
