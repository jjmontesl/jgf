package net.jgf.network.server;

import java.io.IOException;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;

@Configurable
public class ChatConnector extends BaseConnector {
    
    private BaseConnector serverConnector;

    @Override
    public void readConfig(Config config, String configPath) {

        super.readConfig(config, configPath);
        
        String connectorRef = config.getString(configPath + "/connector/@ref");
        Jgf.getDirectory().register(this, "serverConnector", connectorRef);

    }

    
    /**
     * @return the serverConnector
     */
    public final BaseConnector getServerConnector() {
        return serverConnector;
    }

    
    /**
     * @param serverConnector the serverConnector to set
     */
    public final void setServerConnector(BaseConnector serverConnector) {
        this.serverConnector = serverConnector;
    }


    @Override
    public void connect() throws IOException {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void disconnect() {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void sendMessage(JGFMessage message) {
        // TODO Auto-generated method stub
        
    }

}
