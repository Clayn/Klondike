package de.clayntech.klondike.cli;

import de.clayntech.klondike.sdk.err.NameInUseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

public class AddCommandTest extends CommandTest{

    @Test
    public void testInvalidParameter() {
        Assertions.assertThrows(InvalidParameterException.class, () -> new AddCommand().perform(klondike,scanner));
        Assertions.assertThrows(InvalidParameterException.class, () -> new AddCommand().perform(klondike,scanner,"Name"));
    }

    @Test
    public void testAdd() throws Exception {
        new AddCommand().perform(klondike,scanner,"Name","Path");
        Mockito.verify(repository,Mockito.times(1)).register(Mockito.any());
    }

    @Test
    public void testAddNameExists() throws Exception {
        Mockito.doThrow(NameInUseException.class).when(repository).register(Mockito.any());
        Mockito.doNothing().when(klondike).setState(Mockito.anyInt());
        new AddCommand().perform(klondike,scanner,"Name","Path");
        Mockito.verify(klondike,Mockito.times(1)).setState(1001);
        Mockito.verify(repository,Mockito.times(1)).register(Mockito.any());
    }


    @Override
    protected void setup0() {

    }
}
