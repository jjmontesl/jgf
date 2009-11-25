package net.jgf.network.translators;

import java.util.Map;
import java.util.LinkedHashMap;
import net.jgf.translators.Translator;

/**
 * Main translator class. Only has one method, translate.
 * @author Mark Schrijver
 * @version 1.0
 */
public final class TranslatorMap {

    private static final Map <String, Translator> translatorsByKey = new LinkedHashMap <String, Translator>();

    static {
        translatorsByKey.put("net.jgf.network.messages.JGNChatMessage",
                        new net.jgf.network.translators.TranslateJGNChatMessageToJGFChatMessage());
        translatorsByKey.put("net.jgf.messaging.JGFChatMessage",
                        new net.jgf.network.translators.TranslateJGFChatMessageToJGNChatMessage());
    }

    /**
     * Constructor.
     */
    private TranslatorMap() {
    }

    /**
     * Selects the correct translator and uses that translator to translate the supplied message.
     * Only one translator is possible per bean class. If no translator is available null is returned.
     * @param beanToTranslate the bean that needs to be translated.
     * @return the translated bean or null if no translator was available.
     */
    public static Object translate(Object beanToTranslate) {
        Translator translator = translatorsByKey.get(beanToTranslate.getClass().getName());
        if (translator == null) {
            return null;
        }
        return translator.translate(beanToTranslate);
    }
}