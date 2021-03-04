package de.clayntech.klondike.sdk.os;

import de.clayntech.klondike.impl.os.WindowsHandle;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public interface OSHandle {
    void execute(File directory, File executable, boolean processAllowed, Consumer<Integer> resultConsumer) throws IOException;

    default void execute(File directory, File executable, boolean processAllowed) throws IOException {
        execute(directory,executable,processAllowed,(result)->{});
    }

    OSType getOSType();

    static OSHandle getHandle() {
        return new WindowsHandle();
    }
}
