package de.clayntech.klondike.impl.os;

import de.clayntech.klondike.sdk.os.OSHandle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class WindowsHandle implements OSHandle {

    public static final int SUCCESS=0;
    public static final int NO_DESKTOP=1001;


    private void processResult(int result, Consumer<Integer> consumer) {
        if(consumer!=null) {
            consumer.accept(result);
        }
    }

    @Override
    public void execute(File directory, File executable, boolean processAllowed, Consumer<Integer> resultConsumer) throws IOException {
        boolean desktopSupported=Desktop.isDesktopSupported()&&Desktop.getDesktop().isSupported(Desktop.Action.OPEN);
        if(!processAllowed) {
            if(!desktopSupported) {
                processResult(NO_DESKTOP,resultConsumer);
            }else {
                Desktop.getDesktop().open(executable);
                processResult(SUCCESS,resultConsumer);
            }
        }
        boolean process= executable.getName().endsWith(".exe");
        if(!process&&desktopSupported) {
            Desktop.getDesktop().open(executable);
            processResult(SUCCESS,resultConsumer);
        }else {
            Thread t=new Thread(() -> {
                try {
                    Process p=new ProcessBuilder()
                            .directory(directory)
                            .command(executable.toString())
                            .start();
                    ProcessHandle handle=p.toHandle();
                    Thread.currentThread().setName("Klondike-Executor (ID: "+handle.pid()+")");
                    int res=p.waitFor();
                    processResult(res,resultConsumer);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t.setDaemon(true);
            t.start();
        }
    }
}
