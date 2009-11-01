package net.jgf.messages;

import net.jgf.messaging.BaseMessageEventPayload;

/**
 * Base JGF message class. All Messages in the JGF are derived from this one.
 * @author Schrijver
 * @version 1.0
 */
public abstract class BaseJGFMessage extends BaseMessageEventPayload {
    
    /**
     * The category of the message. There are serveral possible categories.
     * These for the most part depend on the type of message.
     * - BROADCAST: Message should be broadcasted to all interested parties.
     * - P2P: Message is intended for a particular recepient.
     * - SERVER: The receiver of the message is responsible for determining what further action needs to be taken.
     * @author Schrijver
     */
    public static enum CATEGORY { BROADCAST, P2P, SERVER }
    
    private long id;
    
    private short playerId;
    
    private CATEGORY category;

    
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
     * @return the category
     */
    public final CATEGORY getCategory() {
        return category;
    }


    
    /**
     * @param category the category to set
     */
    public final void setCategory(CATEGORY category) {
        this.category = category;
    }

    /**
     * @return the player id.
     */
    public short getPlayerId() {
        return this.playerId;
    }

    /**
     * @param playerId the player id to set.
     */
    public void setPlayerId(short playerId) {
        this.playerId = playerId;
    }
    
    
}
