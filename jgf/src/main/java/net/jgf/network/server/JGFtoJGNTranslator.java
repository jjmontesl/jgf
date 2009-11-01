package net.jgf.network.server;

public class JGFtoJGNTranslator {
    
    protected JGFtoJGNTranslator() {
        
    }

    public static JGNMessage translate(JGFMessage jgfMessage) {
        JGFtoJGNTranslator translator = new JGFtoJGNTranslator();
        return translator.translateMessage(jgfMessage);
    }
    
    /**
     * Must be implemented in the extending class.
     * @param jgfMessage message to translate.
     * @return
     */
    protected JGNMessage translateMessage(JGFMessage jgfMessage) {
        return null;
    }
}
