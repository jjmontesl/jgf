package net.jgf.network.messages;

import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.message.type.PlayerMessage;

public class JGNMessage extends Message implements PlayerMessage {

    private short playerId;

    public short getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(short playerId) {
        this.playerId = playerId;
    }
}
