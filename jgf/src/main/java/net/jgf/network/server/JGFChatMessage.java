package net.jgf.network.server;


public class JGFChatMessage extends JGFMessage {
    private String roomId;
    private String text;
    
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
