package net.jgf.translators.network;

public final class TranslateJGFChatMessageToJGNChatMessage implements net.jgf.translators.Translator {

	public TranslateJGFChatMessageToJGNChatMessage(){
	}

	 public Object translate(Object beanInParam) {
		net.jgf.network.server.JGFChatMessage beanIn = (net.jgf.network.server.JGFChatMessage) beanInParam;
		net.jgf.console.bean.JGNChatMessage beanOut = new net.jgf.console.bean.JGNChatMessage();
		beanOut.setRoomId(beanIn.getRoomId());
		beanOut.setText(beanIn.getText());
		beanOut.setPlayerId(beanIn.getPlayerId());
		return beanOut;
	}

//====Missing in net.jgf.console.bean.JGNChatMessage
//setId has no getter in net.jgf.network.server.JGFChatMessage
//setGroupId has no getter in net.jgf.network.server.JGFChatMessage
//setDestinationPlayerId has no getter in net.jgf.network.server.JGFChatMessage
//setMessageClient has no getter in net.jgf.network.server.JGFChatMessage
//setTries has no getter in net.jgf.network.server.JGFChatMessage
//setMaxTries has no getter in net.jgf.network.server.JGFChatMessage
//setTimeout has no getter in net.jgf.network.server.JGFChatMessage
//setTranslatedMessage has no getter in net.jgf.network.server.JGFChatMessage
//setTimestamp has no getter in net.jgf.network.server.JGFChatMessage
//

//====Missing in net.jgf.network.server.JGFChatMessage
//getCategory has no setter in net.jgf.console.bean.JGNChatMessage
//getId has no setter in net.jgf.console.bean.JGNChatMessage
//getClass has no setter in net.jgf.console.bean.JGNChatMessage
//

}