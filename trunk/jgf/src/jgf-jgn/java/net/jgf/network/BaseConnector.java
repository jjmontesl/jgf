package net.jgf.network;

import java.io.IOException;

import net.jgf.core.component.BaseComponent;

/**
 * Base interface for the connectors.
 * @author Schrijver
 * @version 0.1
 */
public abstract class BaseConnector extends BaseComponent {

    /**
     * Connect this connector.
     * @throws IOException
     */
    public abstract void connect() throws IOException;

    /**
     * Disconnect this connector.
     */
    public abstract void disconnect();
}
