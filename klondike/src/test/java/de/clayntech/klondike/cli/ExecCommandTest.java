package de.clayntech.klondike.cli;

import de.clayntech.klondike.impl.KlondikeRunner;
import de.clayntech.klondike.sdk.KlondikeApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.File;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecCommandTest extends CommandTest{
    @Override
    protected void setup0() {
        KlondikeRunner runner= Mockito.mock(KlondikeRunner.class);
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
    public void testExecute() throws Exception {
        KlondikeApplication app1= Mockito.mock(KlondikeApplication.class);
        Mockito.when(app1.getName()).thenReturn("App");
        Mockito.when(app1.getExecutable()).thenReturn(new File(""));
        Mockito.when(repository.getApplications()).thenReturn(Collections.singletonList(app1));
        KlondikeRunner runner = klondike.getRunner();
        Mockito.doNothing().when(runner).execute(Mockito.any());
        new ExecCommand().perform(klondike,scanner,"App");
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
                    count.set(1);
                }
            }
        });
        new ExecCommand().perform(klondike,scanner,"App");
        while(count.get()==0) {
            Thread.sleep(10);
        }
        Assertions.assertEquals(1,count.get());
        Thread.setDefaultUncaughtExceptionHandler(null);
    }
}
