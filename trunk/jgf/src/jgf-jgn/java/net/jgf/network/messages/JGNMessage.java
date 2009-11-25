package net.jgf.network.messages;

import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.message.type.PlayerMessage;

public class JGNMessage extends Message implements PlayerMessage {

    private short clientId;
    
    private String playerKey;

    @Override
    public short getPlayerId() {
        return this.clientId;
    }

    @Override
    public void setPlayerId(short playerId) {
        this.clientId = playerId;
    }

    
    /**
     * @return the playerKey
     */
    public String getPlayerKey() {
        return playerKey;
    }

    
    /**
     * @param playerKey the playerKey to set
     */
    public void setPlayerKey(String playerKey) {
        this.playerKey = playerKey;
    }
}
