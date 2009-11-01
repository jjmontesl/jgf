package net.jgf.translators.network;

public final class TranslateJGNChatMessageToJGFChatMessage implements net.jgf.translators.Translator {

	public TranslateJGNChatMessageToJGFChatMessage(){
	}

	 public Object translate(Object beanInParam) {
		net.jgf.console.bean.JGNChatMessage beanIn = (net.jgf.console.bean.JGNChatMessage) beanInParam;
		net.jgf.network.server.JGFChatMessage beanOut = new net.jgf.network.server.JGFChatMessage();
		beanOut.setRoomId(beanIn.getRoomId());
		beanOut.setText(beanIn.getText());
		beanOut.setPlayerId(beanIn.getPlayerId());
		return beanOut;
	}

//====Missing in net.jgf.network.server.JGFChatMessage
//setId has no getter in net.jgf.console.bean.JGNChatMessage
//setCategory has no getter in net.jgf.console.bean.JGNChatMessage
//

//====Missing in net.jgf.console.bean.JGNChatMessage
//getGroupId has no setter in net.jgf.network.server.JGFChatMessage
//getDestinationPlayerId has no setter in net.jgf.network.server.JGFChatMessage
//getMessageClient has no setter in net.jgf.network.server.JGFChatMessage
//getTries has no setter in net.jgf.network.server.JGFChatMessage
//getMaxTries has no setter in net.jgf.network.server.JGFChatMessage
//getTranslatedMessage has no setter in net.jgf.network.server.JGFChatMessage
//getId has no setter in net.jgf.network.server.JGFChatMessage
//getTimestamp has no setter in net.jgf.network.server.JGFChatMessage
//getTimeout has no setter in net.jgf.network.server.JGFChatMessage
//getClass has no setter in net.jgf.network.server.JGFChatMessage
//

}