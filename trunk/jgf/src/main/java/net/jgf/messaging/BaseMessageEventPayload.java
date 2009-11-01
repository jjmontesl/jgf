package net.jgf.messaging;

/**
 * This class is used to setup the BaseMessageEventPayload in the MessageEvent.
 * The actual payload can be anything, depending on the MESSAGE_EVENT_CATEGORY
 * and the MESSAGE_EVENT_TYPE. The MESSAGE_EVENT_CATEGORY for a particular
 * BaseMessageEventPayload is determined by the BaseMessageEventPayload.
 * @author Schrijver
 * @version 0.1
 */
public abstract class BaseMessageEventPayload {

    private MESSAGE_EVENT_CATEGORY messageEventCategory;

    /**
     * @return the messageEventCategory
     */
    public MESSAGE_EVENT_CATEGORY getMessageEventCategory() {
        return messageEventCategory;
    }

    /**
     * @param messageEventCategory the messageEventCategory to set
     */
    public void setMessageEventCategory(MESSAGE_EVENT_CATEGORY messageEventCategory) {
        this.messageEventCategory = messageEventCategory;
    }

}
