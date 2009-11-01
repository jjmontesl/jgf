package net.jgf.console.bean;

import net.jgf.config.Configurable;
import net.jgf.messaging.MESSAGE_EVENT_CATEGORY;
import net.jgf.messaging.MESSAGE_EVENT_TYPE;
import net.jgf.messaging.MessageEvent;
import net.jgf.messaging.MessageEventKey;
import net.jgf.messaging.MessageEventObserver;
import net.jgf.messaging.MessagingException;
import net.jgf.messaging.PostOffice;
import net.jgf.messaging.payloads.JGFChatMessage;
import net.jgf.network.messages.JGNChatMessage;

import org.apache.log4j.Logger;

/**
 * This bean handles chat messages that are sent and received via the console.
 * @author Schrijver
 * @version 1.0
 */
@Configurable
public class ChatBean implements MessageEventObserver {

    /**
     * Class logger.
     */
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ChatBean.class);

    /**
     * Constructor.
     */
    public ChatBean() {
        try {
            PostOffice.registerMessageEventObserver(
                    new MessageEventKey(MESSAGE_EVENT_TYPE.RECEIVED, MESSAGE_EVENT_CATEGORY.CHAT), this);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the specified message to the ChatConnector.
     * @param text Body of the message.
     */
    public void send(String text) {
        JGFChatMessage message = new JGFChatMessage();
        message.setText(text);
        PostOffice.fireMessageEvent(new MessageEvent(new MessageEventKey(MESSAGE_EVENT_TYPE.SEND,
                MESSAGE_EVENT_CATEGORY.CHAT), message));
    }

    @Override
    public void handleMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageEventKey().getMessageEventType() == MESSAGE_EVENT_TYPE.RECEIVED) {
            logger.debug("chat message received: "
                    + ((JGFChatMessage) messageEvent.getMessageEventPayload()).getText());
        }
    }
}
