package net.jgf.console.bean;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.messaging.BaseJGFMessage;
import net.jgf.messaging.MessageBroker;
import net.jgf.messaging.MessageNotifications;
import net.jgf.messaging.MessagePublisher;
import net.jgf.messaging.MessageSubscriber;
import net.jgf.messaging.payloads.JGFChatMessage;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

/**
 * This bean handles chat messages that are sent and received via the console.
 * @author Schrijver
 * @version 1.0
 */
@Configurable
public class ChatBean implements MessageSubscriber, MessagePublisher {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(ChatBean.class);

    private String id;
    
    private MessageBroker messageBroker;
    
    /**
     * Constructor.
     */
    public ChatBean() {
    }

    /**
     * Send the specified message to the ChatConnector.
     * @param text Body of the message.
     */
    public void send(String text) {
        JGFChatMessage message = new JGFChatMessage();
        message.setTopic("chat_send");
        message.setText(text);
        if (messageBroker == null) {
            logger.error("No messagebroker defined.");
        } else {
            messageBroker.publishMessage(message, id);
        }
    }
    
    /**
     * Reads the config from the xml.
     * @param config handle to the config.
     * @param configPath current xpath.
     */
    public void readConfig(Config config, String configPath) {
        this.id = config.getString(configPath + "/@name");
        if (config.containsKey(configPath + "/messagebroker/@ref")) {
            String messageBrokerRef = config.getString(configPath + "/messagebroker/@ref");
            messageBroker = Jgf.getDirectory().getObjectAs(messageBrokerRef, MessageBroker.class);
            messageBroker.registerMessagePublisher(this);
            int index = 1;
            while (config.containsKey(configPath + "/messagebroker/subscription[" + index + "]/@topic")) {
                String topic = config.getString(configPath + "/messagebroker/subscription[" + index + "]/@topic");
                messageBroker.registerMessageSubscriber(this, topic);
                index++;
            }
        }
    }

    @Override
    public void receiveMessage(BaseJGFMessage message) {
        logger.info("chat message received: "
                + ((JGFChatMessage) message).getText());
    }

    @Override
    public void receiveNotification(BaseJGFMessage message, MessageNotifications notification) {
        if (notification == MessageNotifications.NO_SUBSCRIBERS) {
            logger.debug("No subscribers defined");
        }
    }

    
    @Override
    public String getId() {
        return id;
    }

    
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
