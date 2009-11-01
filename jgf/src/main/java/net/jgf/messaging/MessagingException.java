package net.jgf.messaging;

/**
 * Eception thrown by the Messaging system.
 * @author Schrijver
 * @version 1.0
 */
public class MessagingException extends Exception {

    private static final long serialVersionUID = 5044490356588938630L;

    /**
     * Constructor.
     */
    public MessagingException() {
    }

    /**
     * Constructor.
     * @param message Exception message.
     */
    public MessagingException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param trowable Caused by Exception
     */
    public MessagingException(Throwable trowable) {
        super(trowable);
    }

    /**
     * Constructor.
     * @param message Exception message.
     * @param trowable Caused by Exception.
     */
    public MessagingException(String message, Throwable trowable) {
        super(message, trowable);
    }

}
