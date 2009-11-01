package net.jgf.console.bean;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.network.server.ChatConnector;
import net.jgf.system.Jgf;

@Configurable
public class ChatBean {

    private ChatConnector connector;

    public void readConfig(Config config, String configPath) {

        String connectorRef = config.getString(configPath + "/connector/@ref");
        Jgf.getDirectory().register(this, "chatConnector", connectorRef);

    }

    
    /**
     * @return the connector
     */
    public final ChatConnector getChatConnector() {
        return connector;
    }

    
    /**
     * @param connector the connector to set
     */
    public final void setChatConnector(ChatConnector connector) {
        this.connector = connector;
    }
}
