package org.lld.filesystem.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Directory extends FileSystem{

    private final Map<String, FileSystem> children = new ConcurrentHashMap<>();

    public Directory(String name, FileSystem parent) {
        super(name, parent);
    }

    public void addChild(FileSystem fileSystem){
        children.putIfAbsent(fileSystem.getName(), fileSystem);
    }

    public Map<String, FileSystem> getChildren() {
        return children;
    }

    public FileSystem getChild(String name) {
        return children.get(name);
    }
}
