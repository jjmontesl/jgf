package net.jgf.network.translators;

public final class TranslateJGFChatMessageToJGNChatMessage implements net.jgf.translators.Translator {

	public TranslateJGFChatMessageToJGNChatMessage(){
	}

	 public Object translate(Object beanInParam) {
		net.jgf.messaging.payloads.JGFChatMessage beanIn = (net.jgf.messaging.payloads.JGFChatMessage) beanInParam;
		net.jgf.network.messages.JGNChatMessage beanOut = new net.jgf.network.messages.JGNChatMessage();
		beanOut.setRoomId(beanIn.getRoomId());
		beanOut.setText(beanIn.getText());
		beanOut.setId(beanIn.getId());
		return beanOut;
	}

//====Missing in net.jgf.network.messages.JGNChatMessage
//setPlayerId has no getter in net.jgf.messaging.payloads.JGFChatMessage
//setGroupId has no getter in net.jgf.messaging.payloads.JGFChatMessage
//setDestinationPlayerId has no getter in net.jgf.messaging.payloads.JGFChatMessage
//setMessageClient has no getter in net.jgf.messaging.payloads.JGFChatMessage
//setTries has no getter in net.jgf.messaging.payloads.JGFChatMessage
//setMaxTries has no getter in net.jgf.messaging.payloads.JGFChatMessage
//setTimeout has no getter in net.jgf.messaging.payloads.JGFChatMessage
//setTranslatedMessage has no getter in net.jgf.messaging.payloads.JGFChatMessage
//setTimestamp has no getter in net.jgf.messaging.payloads.JGFChatMessage
//

//====Missing in net.jgf.messaging.payloads.JGFChatMessage
//getTopic has no setter in net.jgf.network.messages.JGNChatMessage
//getClass has no setter in net.jgf.network.messages.JGNChatMessage
//

}