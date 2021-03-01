package de.clayntech.klondike.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KlondikeLoggerFactory {
    private static final int STACK_DEPTH = 3;

    /**
     * Creates a new logger for the calling class. This is a helper method that
     * basically replaces either <br><br>
     *     <ul>
     *         <li>{@code LoggerFactory.getLogger(SomeClass.class);}</li>
     *         <li>{@code LoggerFactory.getLogger(getClass());}</li>
     *     </ul>
     * @return the logger for the calling class generated by the SLF4J LoggerFactory
     */
    public static Logger getLogger() {
        return LoggerFactory.getLogger(getCallingClass());
    }

    private static Class<?> getCallingClass() {
        try {
            return Class.forName(Thread.currentThread().getStackTrace()[STACK_DEPTH].getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
