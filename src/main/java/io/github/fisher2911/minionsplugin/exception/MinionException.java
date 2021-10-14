package io.github.fisher2911.minionsplugin.exception;

public class MinionException extends RuntimeException {

    public MinionException() {
    }

    public MinionException(final String message) {
        super(message);
    }

    public MinionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MinionException(final Throwable cause) {
        super(cause);
    }

    public MinionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
