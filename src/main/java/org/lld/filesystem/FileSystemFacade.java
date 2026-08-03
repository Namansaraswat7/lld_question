package org.lld.filesystem;

import org.lld.filesystem.domain.Directory;
import org.lld.filesystem.domain.File;
import org.lld.filesystem.domain.FileSystem;

public class FileSystemFacade {
    private static volatile FileSystemFacade instance;

    private final Directory root;
    private Directory currentDirectory;

    public FileSystemFacade() {
        this.root = new Directory("/", null);
        this.currentDirectory = root;
    }

    public static FileSystemFacade getInstance() {
        if (instance == null) {
            synchronized (FileSystem.class) {
                if (instance == null) {
                    instance = new FileSystemFacade();
                }
            }
        }
        return instance;
    }

    public void createDirectory(String path) {
        createNode(path, true);
    }
    public void createFile(String path) {
        createNode(path, false);
    }

    public void changeDirectory(String path) {
        FileSystem node = getNode(path);
        if (node instanceof Directory) {
            currentDirectory = (Directory) node;
        } else {
            System.out.println("Error: '" + path + "' is not a directory.");
        }
    }

    public String getWorkingDirectory() {
        return currentDirectory.getPath();
    }

    public void listDirectory(String path) {
        FileSystem node;
        if (path == null || path.isEmpty()) {
            node = currentDirectory;
        } else {
            node = getNode(path);
            if (node == null) {
                System.out.println("Error: '" + path + "' does not exist.");
                return;
            }
        }

        if (node instanceof Directory directory) {
            if (directory.getChildren().isEmpty()) {
                return;
            }
            directory.getChildren().keySet().stream().sorted().forEach(System.out::println);
        } else {
            System.out.println(node.getName());
        }
    }

    private void createNode(String path, boolean isDirectory) {
        String name;
        Directory parent;

        if (path.contains("/")) {
            // Path has directory components (e.g., "/a/b/c" or "b/c")
            int lastSlashIndex = path.lastIndexOf('/');
            name = path.substring(lastSlashIndex + 1);
            String parentPath = path.substring(0, lastSlashIndex);

            // Handle creating in root, e.g., "/testfile"
            if (parentPath.isEmpty()) {
                parentPath = "/";
            }

            FileSystem parentNode = getNode(parentPath);
            if (!(parentNode instanceof Directory)) {
                System.out.println("Error: Invalid path. Parent '" + parentPath + "' is not a directory or does not exist.");
                return;
            }
            parent = (Directory) parentNode;
        } else {
            // Path is a simple name in the current directory (e.g., "c")
            name = path;
            parent = currentDirectory;
        }

        if (name.isEmpty()) {
            System.err.println("Error: File or directory name cannot be empty.");
            return;
        }

        // --- Common logic from here ---
        if (parent.getChild(name) != null) {
            System.out.println("Error: Node '" + name + "' already exists in '" + parent.getPath() + "'.");
            return;
        }

        FileSystem newNode = isDirectory ? new Directory(name, parent) : new File(name, parent);
        parent.addChild(newNode);
    }

    private FileSystem getNode(String path) {
        if (path.equals("/")) return root;

        Directory startDir = path.startsWith("/") ? root : currentDirectory;
        // Use a non-empty string split to handle leading/trailing slashes gracefully
        String[] parts = path.split("/");

        FileSystem current = startDir;
        for (String part : parts) {
            if (part.isEmpty() || part.equals(".")) {
                continue;
            }
            if (!(current instanceof Directory)) {
                return null; // Part of the path is a file, so it's invalid
            }

            if (part.equals("..")) {
                current = current.getParent();
                if (current == null) current = root; // Can't go above root
            } else {
                current = ((Directory) current).getChild(part);
            }

            if (current == null) return null; // Path component does not exist
        }
        return current;
    }
}
