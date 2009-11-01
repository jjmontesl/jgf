package net.jgf.messaging;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Main class for the messaging system. This is where all the observers are
 * registered. All MessageEvents are relayed by this class.
 * @author Schrijver
 * @version 1.0
 */
public final class PostOffice {

    private static Map < MessageEventKey, List < MessageEventObserver >> observerMap = 
        new LinkedHashMap < MessageEventKey, List < MessageEventObserver >>();

    /**
     * Constructor, should never be called directly.
     */
    private PostOffice() {
    }

    /**
     * Register the supplied MessageEventObserver to the supplied
     * MessageEventKey. When an event with the corresponding key is fired, this
     * MessageEventObserver will be notified.
     * @param messageEventKey MessageEventKey to register with.
     * @param messageEventObserver MessageEventObserver to register.
     * @throws MessagingException Something went wrong with the registration
     *             process.
     */
    public static void registerMessageEventObserver(MessageEventKey messageEventKey,
            MessageEventObserver messageEventObserver) throws MessagingException {
        List < MessageEventObserver > observerList = observerMap.get(messageEventKey);
        if (observerList == null) {
            observerList = new LinkedList < MessageEventObserver >();
            observerMap.put(messageEventKey, observerList);
        } else if (observerList.contains(messageEventObserver)) {
            throw new MessagingException("Observer already registered");
        }
        observerList.add(messageEventObserver);
    }

    /**
     * Fires the MessageEvent to the corresponding MessageEventObservers. These
     * events are fire-and-forget. It is not possible to receive realtime
     * response. If a response is required, a new message has to be sent.
     * @param messageEvent The event to be distributed.
     */
    public static void fireMessageEvent(MessageEvent messageEvent) {
        MessageEventKey messageEventKey = messageEvent.getMessageEventKey();
        List < MessageEventObserver > observerList = observerMap.get(messageEventKey);
        if (observerList != null && observerList.size() > 0) {
            for (MessageEventObserver messageEventObserver : observerList) {
                messageEventObserver.handleMessageEvent(messageEvent);
            }
        }
    }
}
