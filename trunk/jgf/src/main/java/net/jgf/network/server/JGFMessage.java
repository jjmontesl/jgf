package net.jgf.network.server;


public abstract class JGFMessage {
    
    public static enum CATEGORY {BROADCAST, P2P, SERVER}
    
    private String id;
    
    private short playerId;
    
    private CATEGORY category;

    
    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }

    
    /**
     * @param id the id to set
     */
    public final void setId(String id) {
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


    public short getPlayerId() {
        return this.playerId;
    }


    public void setPlayerId(short playerId) {
        this.playerId = playerId;
    }
    
    
}
