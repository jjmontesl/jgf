package net.jgf.network.translators;

/**
 * Class to translate between two beans.
 * @author Mark Schrijver
 * @version 1.0
 */
public final class TranslateJGNChatMessageToJGFChatMessage implements net.jgf.translators.Translator {

    /**
     * Constructor.
     */
    public TranslateJGNChatMessageToJGFChatMessage() {
    }

    /**
     * Translation method. Translates net.jgf.network.messages.JGNChatMessage into net.jgf.messaging.JGFChatMessage.
     * @param beanInParam bean to translate.
     * @return translated parameter.
     */
    public Object translate(Object beanInParam) {
        net.jgf.network.messages.JGNChatMessage beanIn = (net.jgf.network.messages.JGNChatMessage) beanInParam;
        net.jgf.messaging.JGFChatMessage beanOut = new net.jgf.messaging.JGFChatMessage();
        beanOut.setText(beanIn.getText());
        beanOut.setRoomId(beanIn.getRoomId());
        beanOut.setPlayerId(beanIn.getPlayerId());
        beanOut.setId(beanIn.getId());
        return beanOut;
    }

//====Missing in net.jgf.messaging.JGFChatMessage
//setTopic has no getter in net.jgf.network.messages.JGNChatMessage
//

//====Missing in net.jgf.network.messages.JGNChatMessage
//getPlayerKey has no setter in net.jgf.messaging.JGFChatMessage
//getTimestamp has no setter in net.jgf.messaging.JGFChatMessage
//getTimeout has no setter in net.jgf.messaging.JGFChatMessage
//getGroupId has no setter in net.jgf.messaging.JGFChatMessage
//getDestinationPlayerId has no setter in net.jgf.messaging.JGFChatMessage
//getMessageClient has no setter in net.jgf.messaging.JGFChatMessage
//getTries has no setter in net.jgf.messaging.JGFChatMessage
//getMaxTries has no setter in net.jgf.messaging.JGFChatMessage
//getTranslatedMessage has no setter in net.jgf.messaging.JGFChatMessage
//getClass has no setter in net.jgf.messaging.JGFChatMessage
//

}