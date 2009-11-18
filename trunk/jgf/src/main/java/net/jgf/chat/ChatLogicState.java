package net.jgf.chat;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.console.bean.ChatBean;
import net.jgf.logic.BaseLogicState;
import net.jgf.messaging.BaseJGFMessage;
import net.jgf.messaging.MessageBroker;
import net.jgf.messaging.MessageNotifications;
import net.jgf.messaging.MessagePublisher;
import net.jgf.messaging.MessageSubscriber;
import net.jgf.messaging.payloads.JGFChatMessage;
import net.jgf.system.Jgf;

/**
 * The main chat logic is put in here. This class accepts characters typed in
 * the ChatViewState and turns them into complete chat lines.
 * @author Schrijver
 * @version 1.0
 */
@Configurable
public class ChatLogicState extends BaseLogicState implements MessageSubscriber, MessagePublisher {

    public static final int SHIFT_LEFT       = 42;

    public static final int SHIFT_RIGHT      = 54;

    public static final int CONTROL_LEFT     = 29;

    public static final int CONTROL_RIGHT    = 157;

    public static final int ALT_LEFT         = 56;

    public static final int ALT_RIGHT        = 184;

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(ChatLogicState.class);

    private boolean         shiftLeftDown    = false;

    private boolean         shiftRightDown   = false;

    private boolean         controlLeftDown  = false;

    private boolean         controlRightDown = false;

    private boolean         altLeftDown      = false;

    private boolean         altRightDown     = false;

    private ChatObserver    chatObserver;

    private String          currentChatMessage;

    private ChatHistory     history          = new ChatHistory(100);
    
    private MessageBroker messageBroker;

    /**
     * Constructor.
     */
    public ChatLogicState() {
        currentChatMessage = "";
    }

    /**
     * called whenever a character is written. Parses the written character to
     * see if it is any of the psecial characters. Currently, only the \r is a
     * special character.
     * @param character character entered by the user.
     */
    public void acceptChar(char character) {
        int charInt = (int) character;
        System.out.println("character = " + charInt);
        switch (charInt) {
            case 0:
                break;
            case 13:
                messageCompleted();
                break;
            default:
                currentChatMessage = currentChatMessage + character;
                break;
        }
    }

    /**
     * Sets the state of one of the special characters outlined in the statics.
     * @param specialCharacter Special character who's state has changed
     * @param state true=down, false = up.
     * @return false if this is not a special character
     */
    public boolean setSpecialCharacterState(int specialCharacter, boolean state) {
        boolean isSpecialCharacter = true;
        switch (specialCharacter) {
            case SHIFT_LEFT:
                shiftLeftDown = state;
                break;
            case SHIFT_RIGHT:
                shiftRightDown = state;
                break;
            case CONTROL_LEFT:
                controlLeftDown = state;
                break;
            case CONTROL_RIGHT:
                controlRightDown = state;
                break;
            case ALT_LEFT:
                altLeftDown = state;
                break;
            case ALT_RIGHT:
                altRightDown = state;
                break;
            default:
                isSpecialCharacter = false;
                break;
        }
        return isSpecialCharacter;
    }

    private void messageCompleted() {
        if (StringUtils.isNotEmpty(currentChatMessage)) {
            history.add(currentChatMessage);
            JGFChatMessage message = new JGFChatMessage();
            message.setTopic(message.getMessageCategory() + "_send");
            message.setText(currentChatMessage);
            if (messageBroker == null) {
                logger.error("No messagebroker defined.");
            } else {
                messageBroker.publishMessage(message, this.getId());
            }
            currentChatMessage = "";
            chatObserver.lineAdded();
        }
    }

    /**
     * returns the last lines from the history.
     * @param lines number of lines to retrieve.
     * @param offset offset where to start retrieving.
     * @return list containing the retrieved lines.
     */
    public List < String > getLastLines(int lines, int offset) {
        return history.get(lines, offset);
    }

    /**
     * return the chat messages that's being written.
     * @return the current chat message.
     */
    public String getCurrentChatMessage() {
        return currentChatMessage;
    }

    /**
     * @return the chatObserver
     */
    public ChatObserver getChatObserver() {
        return chatObserver;
    }

    /**
     * @param chatObserver the chatObserver to set
     */
    public void setChatObserver(ChatObserver chatObserver) {
        this.chatObserver = chatObserver;
    }

    @Override
    public void receiveMessage(BaseJGFMessage message) {
        history.add(((JGFChatMessage) message).getText());
        chatObserver.lineAdded();
    }

    @Override
    public void receiveNotification(BaseJGFMessage message, MessageNotifications notification) {
        if (notification == MessageNotifications.NO_SUBSCRIBERS) {
            logger.debug("No subscribers defined");
        }
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
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
    
    
}
