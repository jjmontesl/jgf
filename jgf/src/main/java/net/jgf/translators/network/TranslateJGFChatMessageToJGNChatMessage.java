package net.jgf.translators.network;

public final class TranslateJGFChatMessageToJGNChatMessage implements net.jgf.translators.Translator {

	public TranslateJGFChatMessageToJGNChatMessage(){
	}

	 public Object translate(Object beanInParam) {
		net.jgf.messaging.payloads.JGFChatMessage beanIn = (net.jgf.messaging.payloads.JGFChatMessage) beanInParam;
		net.jgf.network.messages.JGNChatMessage beanOut = new net.jgf.network.messages.JGNChatMessage();
		beanOut.setRoomId(beanIn.getRoomId());
		beanOut.setText(beanIn.getText());
		beanOut.setPlayerId(beanIn.getPlayerId());
		beanOut.setId(beanIn.getId());
		return beanOut;
	}

//====Missing in net.jgf.console.bean.JGNChatMessage
//setGroupId has no getter in net.jgf.messages.JGFChatMessage
//setDestinationPlayerId has no getter in net.jgf.messages.JGFChatMessage
//setMessageClient has no getter in net.jgf.messages.JGFChatMessage
//setTries has no getter in net.jgf.messages.JGFChatMessage
//setMaxTries has no getter in net.jgf.messages.JGFChatMessage
//setTimeout has no getter in net.jgf.messages.JGFChatMessage
//setTranslatedMessage has no getter in net.jgf.messages.JGFChatMessage
//setTimestamp has no getter in net.jgf.messages.JGFChatMessage
//

//====Missing in net.jgf.messages.JGFChatMessage
//getCategory has no setter in net.jgf.console.bean.JGNChatMessage
//getClass has no setter in net.jgf.console.bean.JGNChatMessage
//

}