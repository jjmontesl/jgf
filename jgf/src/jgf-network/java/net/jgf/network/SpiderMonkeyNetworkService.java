package net.jgf.network;

import java.io.IOException;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.service.BaseService;
import net.jgf.example.tanks.messages.ConnectMessage;
import net.jgf.example.tanks.messages.GameInfoMessage;
import net.jgf.example.tanks.messages.PlayerUpdateMessage;
import net.jgf.example.tanks.messages.SyncRequestMessage;

import org.apache.log4j.Logger;

import com.jme3.network.connection.Client;
import com.jme3.network.connection.Server;
import com.jme3.network.serializing.Serializer;

/**
 * 
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public final class SpiderMonkeyNetworkService extends BaseService {

    private static final int DEFAULT_NETWORK_UDP_PORT = 5050;



    private static final int DEFAULT_NETWORK_TCP_PORT = 4040;



    /**
     * Class logger
     */
    private static final Logger logger = Logger.getLogger(SpiderMonkeyNetworkService.class);

    
    
    private Server server;
    
    private Client client;
    

    public SpiderMonkeyNetworkService() {
        super();
    }

    /**
     * Configures additional rules for the commons-digester library.
     */
    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.getId() + "]";
    }

    @Override
    public void dispose() {
        try {
            if (server != null) {
                server.stop();
            }
        } catch (IOException e) {
            logger.warn("Could not stop network server.", e);
        }

        try {
            if (client != null) {
                client.disconnect();
            }
        } catch (IOException e) {
            logger.warn("Could not stop network client.", e);
        }

        super.dispose();
    }

    public Server startServer() throws IOException {
        server = new Server(DEFAULT_NETWORK_TCP_PORT, DEFAULT_NETWORK_UDP_PORT);
        server.start();
        registerMessages();
        return server;
    }
    
    public Client connect() throws IOException {
        client = new Client("localhost", DEFAULT_NETWORK_TCP_PORT, DEFAULT_NETWORK_UDP_PORT);
        client.start();
        registerMessages();
        return client;
    }

    private void registerMessages() {
        Serializer.registerClass(ConnectMessage.class);
        Serializer.registerClass(GameInfoMessage.class);
        Serializer.registerClass(PlayerUpdateMessage.class);
        Serializer.registerClass(SyncRequestMessage.class);
    }
    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }
    
}
