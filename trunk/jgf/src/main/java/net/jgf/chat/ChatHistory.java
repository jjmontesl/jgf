package net.jgf.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains the Chat History. Automatically limits the history to the specified
 * number of lines.
 * @author Schrijver
 * @version 1.0
 */
public class ChatHistory {

    private final List < String > history;

    private int                   limit = 100;

    /**
     * Constructor.
     * @param limit number of lines to remember,
     */
    public ChatHistory(int limit) {
        this.limit = limit;
        history = new ArrayList < String >(limit);
    }

    /**
     * Add a line to the history.
     * @param chatMessage line to add.
     */
    public void add(String chatMessage) {
        history.add(0, chatMessage);
        if (history.size() > limit) {
            history.remove(limit - 1);
        }
    }

    /**
     * Returns the specified lines from the history.
     * @param lines number of lines requested
     * @param offset offset where to start in the history.
     * @return lines found. This can be a smaller amount of lines then requested.
     */
    public List < String > get(int lines, int offset) {
        List < String > result = new ArrayList < String >();
        for (int i = 0; i < lines; i++) {
            if (offset + i < history.size()) {
                result.add(history.get(offset + i));
            }
        }
        Collections.reverse(result);
        return result;
    }
}
