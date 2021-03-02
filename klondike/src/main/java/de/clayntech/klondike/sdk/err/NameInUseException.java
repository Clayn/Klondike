package de.clayntech.klondike.sdk.err;

public class NameInUseException extends RuntimeException{
    public NameInUseException() {
    }

    public NameInUseException(String message) {
        super(message);
    }

    public NameInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameInUseException(Throwable cause) {
        super(cause);
    }
}
