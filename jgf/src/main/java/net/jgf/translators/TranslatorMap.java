package net.jgf.translators;

import java.util.LinkedHashMap;

public final class TranslatorMap {

	private static final LinkedHashMap <String, Translator> translatorsByKey = new LinkedHashMap <String, Translator>();

	private TranslatorMap() {
		translatorsByKey.put("net.jgf.console.bean.JGNChatMessage", new net.jgf.translators.network.TranslateJGNChatMessageToJGFChatMessage());
		translatorsByKey.put("net.jgf.messages.JGFChatMessage", new net.jgf.translators.network.TranslateJGFChatMessageToJGNChatMessage());
	}

	public static Object translate(Object beanToTranslate) {
		return translatorsByKey.get(beanToTranslate.getClass().getName()).translate(beanToTranslate);
	}
}