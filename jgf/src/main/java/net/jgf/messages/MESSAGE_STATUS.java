package net.jgf.messages;

/**
 * Network status of the message.
 * @author Schrijver
 * @version 1.0
 */
public enum MESSAGE_STATUS {
    /**
     * Message has failed. This means it cannot be processed by the receiving party for some reason.
     */
    FAILED,
    /**
     * The receiving party has accepted the message.
     */
    ACCEPTED,
    /**
     * The receiving party has processed the message.
     */
    PROCESSED
}
