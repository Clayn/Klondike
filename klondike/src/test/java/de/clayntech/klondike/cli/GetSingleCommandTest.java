package de.clayntech.klondike.cli;

import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.util.Formatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class GetSingleCommandTest extends CommandTest{
    @Override
    protected void setup0() {

    }

    @Test
    public void testArgInvalid() {
        Assertions.assertThrows(InvalidParameterException.class, () -> new GetSingleApplication().perform(klondike,scanner));
    }

    @Test
    public void testNoApplication() {
        Mockito.when(repository.getApplications()).thenReturn(Collections.emptyList());
        Assertions.assertThrows(IllegalArgumentException.class, () -> new GetSingleApplication().perform(klondike,scanner,"App"));
    }

    static class TestFormatter implements Formatter<KlondikeApplication> {

        private static final AtomicInteger toStringCount=new AtomicInteger(0);
        private static final AtomicInteger fromStringCount=new AtomicInteger(0);

        public TestFormatter() {
        }

        @Override
        public String toString(KlondikeApplication val) {
            toStringCount.incrementAndGet();
            return "";
        }

        @Override
        public KlondikeApplication fromString(String str) {
            fromStringCount.incrementAndGet();
            return null;
        }

        public static void reset() {
            toStringCount.set(0);
            fromStringCount.set(0);
        }

    }

    @Test
    public void testGetApplication() throws Exception {
        TestFormatter.reset();
        String formatterName=TestFormatter.class.getName();
        KlondikeApplication app=Mockito.mock(KlondikeApplication.class);
        Mockito.when(app.getName()).thenReturn("Test");
        Mockito.when(repository.getApplications()).thenReturn(Collections.singletonList(app));
        new GetSingleApplication().perform(klondike,scanner,formatterName,"Test");
        Assertions.assertEquals(1,TestFormatter.toStringCount.get());
    }
}
