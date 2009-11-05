package net.jgf.messaging.payloads;

import net.jgf.messaging.BaseJGFMessage;

/**
 * Message meant for chatting. This message is destined for the network layer.
 * @author Schrijver
 * @version 1.0
 */
public class JGFChatMessage extends BaseJGFMessage {
    private String roomId;
    private String text;
    
    /**
     * Constructor.
     */
    public JGFChatMessage() {
    }
    /**
     * @return the roomId
     */
    public final String getRoomId() {
        return roomId;
    }
    
    /**
     * @param roomId the roomId to set
     */
    public final void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    /**
     * @return the text
     */
    public final String getText() {
        return text;
    }
    
    /**
     * @param text the text to set
     */
    public final void setText(String text) {
        this.text = text;
    }
}
