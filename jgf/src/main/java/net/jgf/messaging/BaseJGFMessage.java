package net.jgf.messaging;


/**
 * Base JGF message class. All Messages in the JGF are derived from this one.
 * @author Schrijver
 * @version 1.0
 */
public abstract class BaseJGFMessage {
    
    private long id;
    
    private String topic;
    
    private short playerId;
    
    /**
     * @return the id
     */
    public final long getId() {
        return id;
    }

    
    /**
     * @param id the id to set
     */
    public final void setId(long id) {
        this.id = id;
    }


    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }


    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }
    
    /**
     * Returns the category of the message.
     * @return the category.
     */
    public abstract String getMessageCategory();


    
    /**
     * @return the playerId
     */
    public short getPlayerId() {
        return playerId;
    }


    
    /**
     * @param playerId the playerId to set
     */
    public void setPlayerId(short playerId) {
        this.playerId = playerId;
    }
    
}
