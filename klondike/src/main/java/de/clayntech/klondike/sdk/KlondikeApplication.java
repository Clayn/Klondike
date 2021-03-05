package de.clayntech.klondike.sdk;

import de.clayntech.klondike.sdk.exec.ExecutionScript;

import java.io.File;

public interface KlondikeApplication {
    ExecutionScript getScript();

    String getName();

    File getExecutable();

    void setName(String name);

    void setExecutable(File executable);
}
