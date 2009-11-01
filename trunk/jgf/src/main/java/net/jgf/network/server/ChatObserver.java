package net.jgf.network.server;


public interface ChatObserver {
    void chatEventReceived(ChatEvent event);
}
