package net.jgf.chat;

/**
 * Interface used to observer the ChatLogicState for changes.
 * @author Schrijver
 * @version 1.0
 */
public interface ChatObserver {

    /**
     * A line has been added to the underlying logic state.
     */
    void lineAdded();
}
