package net.jgf.network.translators;

import java.util.LinkedHashMap;
import net.jgf.translators.Translator;

public final class TranslatorMap {

	private static final LinkedHashMap <String, Translator> translatorsByKey = new LinkedHashMap <String, Translator>();

	private TranslatorMap() {
		translatorsByKey.put("net.jgf.network.messages.JGNChatMessage", new net.jgf.network.translators.TranslateJGNChatMessageToJGFChatMessage());
		translatorsByKey.put("net.jgf.messaging.payloads.JGFChatMessage", new net.jgf.network.translators.TranslateJGFChatMessageToJGNChatMessage());
	}

	public static Object translate(Object beanToTranslate) {
		return translatorsByKey.get(beanToTranslate.getClass().getName()).translate(beanToTranslate);
	}
}