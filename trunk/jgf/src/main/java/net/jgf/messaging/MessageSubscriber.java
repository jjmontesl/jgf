package net.jgf.messaging;


public interface MessageSubscriber {

    void receiveMessage(BaseJGFMessage message);

}
