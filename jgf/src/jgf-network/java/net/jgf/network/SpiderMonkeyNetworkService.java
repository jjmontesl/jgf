package net.jgf.network;

import java.io.IOException;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.service.BaseService;
import net.jgf.core.service.ServiceException;

import org.apache.log4j.Logger;

import com.jme3.network.connection.Client;
import com.jme3.network.connection.Server;

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
        super.dispose();
    }

    public Server startServer() throws ServiceException {
        try {
            server = new Server(DEFAULT_NETWORK_TCP_PORT, DEFAULT_NETWORK_UDP_PORT);
            server.start();
        } catch (IOException e) {
            throw new ServiceException("Could not create network server", e);
        }
        return server;
    }
    
    public Client connect() {
        try {
            client = new Client("localhost", DEFAULT_NETWORK_TCP_PORT, DEFAULT_NETWORK_UDP_PORT);
            client.start();
        } catch (IOException e) {
            throw new ServiceException("Could not create client connection", e);
        }
        return client;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }
    
}
