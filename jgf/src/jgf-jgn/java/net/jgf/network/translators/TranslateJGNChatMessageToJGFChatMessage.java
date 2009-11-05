package net.jgf.network.translators;

public final class TranslateJGNChatMessageToJGFChatMessage implements net.jgf.translators.Translator {

	public TranslateJGNChatMessageToJGFChatMessage(){
	}

	 public Object translate(Object beanInParam) {
		net.jgf.network.messages.JGNChatMessage beanIn = (net.jgf.network.messages.JGNChatMessage) beanInParam;
		net.jgf.messaging.payloads.JGFChatMessage beanOut = new net.jgf.messaging.payloads.JGFChatMessage();
		beanOut.setRoomId(beanIn.getRoomId());
		beanOut.setText(beanIn.getText());
		beanOut.setId(beanIn.getId());
		return beanOut;
	}

//====Missing in net.jgf.messaging.payloads.JGFChatMessage
//setTopic has no getter in net.jgf.network.messages.JGNChatMessage
//

//====Missing in net.jgf.network.messages.JGNChatMessage
//getPlayerId has no setter in net.jgf.messaging.payloads.JGFChatMessage
//getGroupId has no setter in net.jgf.messaging.payloads.JGFChatMessage
//getDestinationPlayerId has no setter in net.jgf.messaging.payloads.JGFChatMessage
//getMessageClient has no setter in net.jgf.messaging.payloads.JGFChatMessage
//getTries has no setter in net.jgf.messaging.payloads.JGFChatMessage
//getMaxTries has no setter in net.jgf.messaging.payloads.JGFChatMessage
//getTranslatedMessage has no setter in net.jgf.messaging.payloads.JGFChatMessage
//getTimestamp has no setter in net.jgf.messaging.payloads.JGFChatMessage
//getTimeout has no setter in net.jgf.messaging.payloads.JGFChatMessage
//getClass has no setter in net.jgf.messaging.payloads.JGFChatMessage
//

}