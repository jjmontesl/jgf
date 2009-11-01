package net.jgf.network.server;

import net.jgf.messages.BaseJGFMessage;
import net.jgf.messages.MESSAGE_STATUS;

/**
 * Observes the specified connector for events.
 * @author Schrijver
 * @version 1.0
 */
public interface ConnectorObserver {

    /**
     * A message has been received by the BaseConnector being observer.
     * @param message The received message.
     */
    void messageReceived(BaseJGFMessage message);
    
    /**
     * The status of a message has changed.
     * @param messageId The ID of the message for which the status change has occured.
     * @param status The new status.
     */
    void statusUpdate(long messageId, MESSAGE_STATUS status);

}
