package net.jgf.messaging;

/**
 * This class contains the information about the MessageEvent.
 * @author Schrijver
 * @version 1.0
 */
public class MessageEvent {

    private MessageEventKey     messageEventKey;

    private BaseMessageEventPayload messageEventPayload;

    /**
     * Constructor.
     * @param messageEventKey The MessageEventKey identifying this MessageEvent.
     * @param messageEventPayload The BaseMessageEventPayload containing the actual content of the message.
     */
    public MessageEvent(MessageEventKey messageEventKey, BaseMessageEventPayload messageEventPayload) {
        setMessageEventKey(messageEventKey);
        setMessageEventPayload(messageEventPayload);
    }

    /**
     * @return the messageEventKey
     */
    public  MessageEventKey getMessageEventKey() {
        return messageEventKey;
    }

    /**
     * @param messageEventKey the messageEventKey to set
     */
    private  void setMessageEventKey(MessageEventKey messageEventKey) {
        this.messageEventKey = messageEventKey;
    }

    /**
     * @return the messageEventPayload
     */
    public  BaseMessageEventPayload getMessageEventPayload() {
        return messageEventPayload;
    }

    /**
     * @param messageEventPayload the messageEventPayload to set
     */
    private  void setMessageEventPayload(BaseMessageEventPayload messageEventPayload) {
        this.messageEventPayload = messageEventPayload;
    }

}
