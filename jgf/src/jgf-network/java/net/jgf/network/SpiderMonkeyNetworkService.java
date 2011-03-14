package net.jgf.network;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.service.BaseService;
import net.jgf.core.service.ServiceException;

import org.apache.log4j.Logger;

import com.jme3.network.connection.Client;
import com.jme3.network.connection.Server;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializer;

/**
 * 
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public final class SpiderMonkeyNetworkService extends BaseService {

    private static final String DEFAULT_NETWORK_HOST = "jgf";



    private static final int DEFAULT_NETWORK_UDP_PORT = 5050;



    private static final int DEFAULT_NETWORK_TCP_PORT = 4040;



    /**
     * Class logger
     */
    private static final Logger logger = Logger.getLogger(SpiderMonkeyNetworkService.class);

    
    
    private Server server;
    
    private Client client;
    
    private List<Class<? extends Message>> messageClasses = new ArrayList<Class<? extends Message>>();
    

    public SpiderMonkeyNetworkService() {
        super();
    }

    
    
    @Override
    public void initialize() {
        super.initialize();
        registerMessages();
    }


    public void addMessageClass(Class<? extends Message> clazz) {
        this.messageClasses.add(clazz);
    }

    /**
     * Configures additional rules for the commons-digester library.
     */
    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
        
        int index = 1;
        while (config.containsKey(configPath + "/messageClasses/message[" + index + "]/@class")) {
            String clazz = config.getString(configPath + "/messageClasses/message[" + index + "]/@class");
            // TODO: Check type safety
            try {
                this.addMessageClass((Class<? extends Message>) Class.forName(clazz));
            } catch (ClassNotFoundException e) {
                throw new ServiceException ("Invalid message class name (" + clazz+ ") when reading configuration of " + this, e);
            }
            index++;
        }
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
                try {
                    client.disconnect();
                } catch (NotYetConnectedException e) {
                    logger.debug("Could not disconnect client since it was not yet connected.");
                } 
            }
        } catch (IOException e) {
            logger.warn("Could not stop network client.", e);
        } catch (NullPointerException e) {
            logger.error("NPE while disconnecting client (maybe due to threading issues?). Swallowing exception.", e);
        }

        super.dispose();
    }

    public Server startServer() throws IOException {
        logger.info("Starting server on ports TCP " + DEFAULT_NETWORK_TCP_PORT + " and UDP " + DEFAULT_NETWORK_UDP_PORT);
        server = new Server(DEFAULT_NETWORK_TCP_PORT, DEFAULT_NETWORK_UDP_PORT);
        server.start();
        return server;
    }
    
    public Client connect() throws IOException {
        logger.info("Connecting to " + DEFAULT_NETWORK_HOST + " TCP " + DEFAULT_NETWORK_TCP_PORT + " and UDP " + DEFAULT_NETWORK_UDP_PORT);
        client = new Client(DEFAULT_NETWORK_HOST, DEFAULT_NETWORK_TCP_PORT, DEFAULT_NETWORK_UDP_PORT);
        client.start();
        return client;
    }

    private void registerMessages() {
        for (Class<? extends Message> messageClass : messageClasses) {
            logger.debug("Registering message class: " + messageClass.getCanonicalName());
            Serializer.registerClass(messageClass);
        }
    }
    
    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }
    
}
