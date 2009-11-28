package net.jgf.network.translators;

/**
 * Class to translate between two beans.
 * @author Mark Schrijver
 * @version 1.0
 */
public final class TranslateJGFChatMessageToJGNChatMessage implements net.jgf.translators.Translator {

    /**
     * Constructor.
     */
    public TranslateJGFChatMessageToJGNChatMessage() {
    }

    /**
     * Translation method. Translates net.jgf.messaging.JGFChatMessage into net.jgf.network.messages.JGNChatMessage.
     * @param beanInParam bean to translate.
     * @return translated parameter.
     */
    public Object translate(Object beanInParam) {
        net.jgf.messaging.JGFChatMessage beanIn = (net.jgf.messaging.JGFChatMessage) beanInParam;
        net.jgf.network.messages.JGNChatMessage beanOut = new net.jgf.network.messages.JGNChatMessage();
        beanOut.setText(beanIn.getText());
        beanOut.setRoomId(beanIn.getRoomId());
        beanOut.setId(beanIn.getId());
        beanOut.setPlayerId(beanIn.getPlayerId());
        return beanOut;
    }

//====Missing in net.jgf.network.messages.JGNChatMessage
//setPlayerKey has no getter in net.jgf.messaging.JGFChatMessage
//setTimestamp has no getter in net.jgf.messaging.JGFChatMessage
//setGroupId has no getter in net.jgf.messaging.JGFChatMessage
//setDestinationPlayerId has no getter in net.jgf.messaging.JGFChatMessage
//setMessageClient has no getter in net.jgf.messaging.JGFChatMessage
//setTries has no getter in net.jgf.messaging.JGFChatMessage
//setMaxTries has no getter in net.jgf.messaging.JGFChatMessage
//setTimeout has no getter in net.jgf.messaging.JGFChatMessage
//setTranslatedMessage has no getter in net.jgf.messaging.JGFChatMessage
//

//====Missing in net.jgf.messaging.JGFChatMessage
//getMessageCategory has no setter in net.jgf.network.messages.JGNChatMessage
//getTopic has no setter in net.jgf.network.messages.JGNChatMessage
//getClass has no setter in net.jgf.network.messages.JGNChatMessage
//

}