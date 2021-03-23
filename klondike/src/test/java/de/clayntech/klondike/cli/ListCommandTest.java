package de.clayntech.klondike.cli;

import de.clayntech.klondike.sdk.KlondikeApplication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class ListCommandTest extends CommandTest{
    @Override
    protected void setup0() {

    }

    @Test
    public void testList() {
        KlondikeApplication app1= Mockito.mock(KlondikeApplication.class);
        Mockito.when(app1.getName()).thenReturn("App 1");
        Mockito.when(app1.getExecutable()).thenReturn(new File(""));

        KlondikeApplication app2= Mockito.mock(KlondikeApplication.class);
        Mockito.when(app2.getName()).thenReturn("App 2");
        Mockito.when(app2.getExecutable()).thenReturn(new File(""));

        Mockito.when(repository.getApplications()).thenReturn(Arrays.asList(app1,app2));
        new ListCommand().perform(klondike,scanner);
        Mockito.verify(repository,Mockito.times(1)).getApplications();
    }

    @Test
    public void testListEmpty() {
        Mockito.when(repository.getApplications()).thenReturn(Collections.emptyList());
        new ListCommand().perform(klondike,scanner);
        Mockito.verify(repository,Mockito.times(1)).getApplications();
    }
}
