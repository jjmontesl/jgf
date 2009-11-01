package net.jgf.translators.network;

public final class TranslateJGNChatMessageToJGFChatMessage implements net.jgf.translators.Translator {

	public TranslateJGNChatMessageToJGFChatMessage(){
	}

	 public Object translate(Object beanInParam) {
		net.jgf.network.messages.JGNChatMessage beanIn = (net.jgf.network.messages.JGNChatMessage) beanInParam;
		net.jgf.messaging.payloads.JGFChatMessage beanOut = new net.jgf.messaging.payloads.JGFChatMessage();
		beanOut.setRoomId(beanIn.getRoomId());
		beanOut.setText(beanIn.getText());
		beanOut.setPlayerId(beanIn.getPlayerId());
		beanOut.setId(beanIn.getId());
		return beanOut;
	}

//====Missing in net.jgf.messages.JGFChatMessage
//setCategory has no getter in net.jgf.console.bean.JGNChatMessage
//

//====Missing in net.jgf.console.bean.JGNChatMessage
//getGroupId has no setter in net.jgf.messages.JGFChatMessage
//getDestinationPlayerId has no setter in net.jgf.messages.JGFChatMessage
//getMessageClient has no setter in net.jgf.messages.JGFChatMessage
//getTries has no setter in net.jgf.messages.JGFChatMessage
//getMaxTries has no setter in net.jgf.messages.JGFChatMessage
//getTranslatedMessage has no setter in net.jgf.messages.JGFChatMessage
//getTimestamp has no setter in net.jgf.messages.JGFChatMessage
//getTimeout has no setter in net.jgf.messages.JGFChatMessage
//getClass has no setter in net.jgf.messages.JGFChatMessage
//

}