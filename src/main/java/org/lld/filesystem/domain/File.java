package org.lld.filesystem.domain;

public class File extends  FileSystem {
    private String content;
    // metadata, updatedby, extension

    public File(String name, FileSystem parent) {
        super(name, parent);
        this.content = "";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
