package org.lld.filesystem.domain;

import java.time.Instant;
import java.time.LocalDateTime;

public abstract class FileSystem {
    private String name;
    private FileSystem parent;
    private LocalDateTime createdAt;

    public FileSystem(String name, FileSystem parent) {
        this.name = name;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
    }

    public String getPath() {
        if(parent == null) {
            return name;
        }

        // Avoid double slash for root's children
        if (parent.getParent() == null) {
            return parent.getPath() + name;
        }
        return parent.getPath() + "/" + name;
    }

    public String getName() {
        return name;
    }

    public FileSystem getParent() {
        return parent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
