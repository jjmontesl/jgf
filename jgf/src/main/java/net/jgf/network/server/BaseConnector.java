package net.jgf.network.server;

import java.io.IOException;

import net.jgf.core.component.BaseComponent;

/**
 * Base class for the connectors.
 * @author Schrijver
 *
 */
public abstract class BaseConnector extends BaseComponent {

    private ConnectorObserver observer;

    /**
     * set the observer for this Connector. Currently, only one oberserver is allowed.
     * @param observer
     */
    public void setConnectorObserver(ConnectorObserver observer) {
        this.observer = observer;
    }

    /**
     * get the observer for this Connector. Currently, only one oberserver is allowed.
     * @param observer
     */
    public ConnectorObserver getConnectorObserver() {
        return observer;
    }
    
    /**
     * Connect this connector.
     * @throws IOException 
     */
    public abstract void connect() throws IOException;
    
    /**
     * Disconnect this connector.
     */
    public abstract void disconnect();
    
    public abstract void sendMessage(JGFMessage message);
}
