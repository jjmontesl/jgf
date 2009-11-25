package net.jgf.network.translators;

public final class TranslateJGFChatMessageToJGNChatMessage implements net.jgf.translators.Translator {

	public TranslateJGFChatMessageToJGNChatMessage(){
	}

	 public Object translate(Object beanInParam) {
		net.jgf.messaging.JGFChatMessage beanIn = (net.jgf.messaging.JGFChatMessage) beanInParam;
		net.jgf.network.messages.JGNChatMessage beanOut = new net.jgf.network.messages.JGNChatMessage();
		beanOut.setRoomId(beanIn.getRoomId());
		beanOut.setText(beanIn.getText());
		beanOut.setPlayerId(beanIn.getPlayerId());
		beanOut.setId(beanIn.getId());
		return beanOut;
	}

//====Missing in net.jgf.network.messages.JGNChatMessage
//setPlayerKey has no getter in net.jgf.messaging.JGFChatMessage
//setGroupId has no getter in net.jgf.messaging.JGFChatMessage
//setDestinationPlayerId has no getter in net.jgf.messaging.JGFChatMessage
//setMessageClient has no getter in net.jgf.messaging.JGFChatMessage
//setTries has no getter in net.jgf.messaging.JGFChatMessage
//setMaxTries has no getter in net.jgf.messaging.JGFChatMessage
//setTimeout has no getter in net.jgf.messaging.JGFChatMessage
//setTranslatedMessage has no getter in net.jgf.messaging.JGFChatMessage
//setTimestamp has no getter in net.jgf.messaging.JGFChatMessage
//

//====Missing in net.jgf.messaging.JGFChatMessage
//getMessageCategory has no setter in net.jgf.network.messages.JGNChatMessage
//getTopic has no setter in net.jgf.network.messages.JGNChatMessage
//getClass has no setter in net.jgf.network.messages.JGNChatMessage
//

}