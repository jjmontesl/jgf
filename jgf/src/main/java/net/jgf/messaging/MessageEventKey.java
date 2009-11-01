package net.jgf.messaging;

/**
 * Key by which a message event can be uniquely defined.
 * This key is also used to determine which MessageEventObservers have subscribed to the event.
 * @author Schrijver
 * @version 1.0
 */
public class MessageEventKey {
    
    private MESSAGE_EVENT_TYPE messageEventType;
    private MESSAGE_EVENT_CATEGORY messageEventCategory;
    
    private int hashCode;
    
    /**
     * Constructor.
     * @param messageEventType Type of the event.
     * @param messageEventCategory category of the event.
     */
    public MessageEventKey(MESSAGE_EVENT_TYPE messageEventType, MESSAGE_EVENT_CATEGORY messageEventCategory) {
        setMessageEventType(messageEventType);
        setMessageEventCategory(messageEventCategory);
        hashCode = (getMessageEventType().toString() + getMessageEventCategory().toString()).hashCode();
    }

    
    /**
     * @return the messageEventType
     */
    public  MESSAGE_EVENT_TYPE getMessageEventType() {
        return messageEventType;
    }

    
    /**
     * @param messageEventType the messageEventType to set
     */
    private void setMessageEventType(MESSAGE_EVENT_TYPE messageEventType) {
        this.messageEventType = messageEventType;
    }

    
    /**
     * @return the messageEventCategory
     */
    public  MESSAGE_EVENT_CATEGORY getMessageEventCategory() {
        return messageEventCategory;
    }

    
    /**
     * @param messageEventCategory the messageEventCategory to set
     */
    private  void setMessageEventCategory(MESSAGE_EVENT_CATEGORY messageEventCategory) {
        this.messageEventCategory = messageEventCategory;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return hashCode;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
    
    
}
