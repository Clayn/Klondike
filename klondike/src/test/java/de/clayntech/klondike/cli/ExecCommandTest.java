package de.clayntech.klondike.cli;

import de.clayntech.klondike.impl.KlondikeRunner;
import de.clayntech.klondike.sdk.KlondikeApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecCommandTest extends CommandTest{
    @Override
    protected void setup0() {
        KlondikeRunner runner= Mockito.spy(new KlondikeRunner());
        Mockito.when(klondike.getRunner()).thenReturn(runner);
    }

    @Test
    public void testInvalidParameter() {
        Assertions.assertThrows(InvalidParameterException.class, () -> new ExecCommand().perform(klondike,scanner));
    }

    @Test
    public void testNoApplication() {
        Mockito.when(repository.getApplications()).thenReturn(Collections.emptyList());
        new ExecCommand().perform(klondike,scanner,"App");
        Mockito.verify(repository,Mockito.times(1)).getApplications();
    }

    @Test
    @Timeout(2)
    public void testExecute() throws Exception {
        KlondikeApplication app1= Mockito.mock(KlondikeApplication.class);
        Mockito.when(app1.getName()).thenReturn("App");
        Mockito.when(app1.getExecutable()).thenReturn(new File(""));
        Mockito.when(repository.getApplications()).thenReturn(Collections.singletonList(app1));
        KlondikeRunner runner = klondike.getRunner();

        Object lock=new Object();
        Mockito.doAnswer(invocationOnMock -> {
            synchronized (lock) {
                lock.notifyAll();
            }
            return null;
        }).when(runner).execute(Mockito.any());
        synchronized (lock) {
            new ExecCommand().perform(klondike,scanner,"App");
            lock.wait();
        }
        Mockito.verify(runner,Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    @Timeout(2)
    public void testExecuteError() throws Exception {
        KlondikeApplication app1= Mockito.mock(KlondikeApplication.class);
        Mockito.when(app1.getName()).thenReturn("App");
        Mockito.when(app1.getExecutable()).thenReturn(new File(""));
        Mockito.when(repository.getApplications()).thenReturn(Collections.singletonList(app1));
        KlondikeRunner runner = klondike.getRunner();
        AtomicInteger count=new AtomicInteger(0);
        Mockito.doThrow(new Exception("Test")).when(runner).execute(Mockito.any());
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            if(e.getCause() instanceof Exception) {
                if("Test".equals(e.getCause().getMessage())) {
                    synchronized (count) {
                        count.set(1);
                        count.notify();
                    }
                }
            }
        });
        synchronized (count) {
            new ExecCommand().perform(klondike,scanner,"App");
            count.wait();
        }
        Assertions.assertEquals(1,count.get());
        Thread.setDefaultUncaughtExceptionHandler(null);
    }
}
