package net.jgf.network.server;

import java.util.List;


public class ChatEvent {
    enum EVENT_TYPE {SEND_MESSAGE, RECEIVED_MESSAGE, ENTER_ROOM, LEAVE_ROOM, ROOM_MEMBER_LIST}
    
    private String roomId;
    private String playerId;
    private String message;
    private List<String> roomMemberList;
    private EVENT_TYPE eventType;
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
     * @return the playerId
     */
    public final String getPlayerId() {
        return playerId;
    }
    
    /**
     * @param playerId the playerId to set
     */
    public final void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    
    /**
     * @return the message
     */
    public final String getMessage() {
        return message;
    }
    
    /**
     * @param message the message to set
     */
    public final void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * @return the roomMemberList
     */
    public final List < String > getRoomMemberList() {
        return roomMemberList;
    }
    
    /**
     * @param roomMemberList the roomMemberList to set
     */
    public final void setRoomMemberList(List < String > roomMemberList) {
        this.roomMemberList = roomMemberList;
    }

    public final void setEventType(EVENT_TYPE eventType) {
        this.eventType = eventType;
    }

    public final EVENT_TYPE getEventType() {
        return eventType;
    }
    
    
}
