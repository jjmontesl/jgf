package net.jgf.messaging;


public interface MessagePublisher {
    
    String getId();

    void receiveNotification(BaseJGFMessage message, MessageNotifications no_subscribers);

}
