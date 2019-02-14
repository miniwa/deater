package se.miniwa.deater.mail;

public class InvalidEmailAddressException extends EmailException {
    public InvalidEmailAddressException() {
    }

    public InvalidEmailAddressException(String message) {
        super(message);
    }

    public InvalidEmailAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEmailAddressException(Throwable cause) {
        super(cause);
    }

    public InvalidEmailAddressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
