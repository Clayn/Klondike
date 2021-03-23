package de.clayntech.klondike.cli;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.io.PrintStream;

public class EchoCommandTest extends CommandTest{
    @Override
    protected void setup0() {

    }

    @Test
    public void testEcho() {
        PrintStream printStream=System.out;
        PrintStream mockStream= Mockito.mock(PrintStream.class);
        System.setOut(mockStream);
        new EchoCommand().perform(klondike,scanner,"Hello","World");
        Mockito.verify(mockStream,Mockito.atLeast(1)).println(Mockito.anyString());
        Mockito.verify(mockStream).println(Mockito.argThat((ArgumentMatcher<String>) t -> t.equals("Hello World")));
        System.setOut(printStream);
    }
}
