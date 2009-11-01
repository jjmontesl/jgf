package net.jgf.messaging;

/**
 * Interface to be implemented by classes that want to subscribe to events from the PostOffice.
 * @author Schrijver
 * @version 1.0
 */
public interface MessageEventObserver {
    
    /**
     * Through this method the implementing class can handle the incoming MessageEvent.
     * @param messageEvent Event to be handled.
     */
    void handleMessageEvent(MessageEvent messageEvent);
}
