package org.lld.filesystem.command;

import org.lld.filesystem.FileSystemFacade;

public class LsCommand implements Command {
    private final FileSystemFacade fs;
    private final String path;

    public LsCommand(FileSystemFacade fs, String path) {
        this.fs = fs;
        this.path = path;
    }

    @Override
    public void execute() {
        fs.listDirectory(path);
    }
}
