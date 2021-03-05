package de.clayntech.klondike.sdk.param.types;

import java.io.File;

public class Directory {

    private final File directory;

    public Directory(File directory) {
        this.directory = directory;
    }

    public File getDirectory() {
        return directory;
    }
}
