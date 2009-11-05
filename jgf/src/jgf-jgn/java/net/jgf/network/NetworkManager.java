package net.jgf.network;

import java.io.IOException;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.network.server.BaseConnector;
import net.jgf.system.Jgf;

/**
 * Responsible for registering all the different connectors with the registry.
 * @author Schrijver
 */
@Configurable
public class NetworkManager extends BaseService {

    private List < BaseConnector > connectorList;

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
        this.connectorList = ConfigurableFactory.newListFromConfig(config, configPath + "/connector",
                BaseConnector.class);
        for (BaseConnector connector : connectorList) {
            Jgf.getDirectory().addObject(connector.getId(), connector);
        }
    }

    /* (non-Javadoc)
     * @see net.jgf.core.service.BaseService#initialize()
     */
    @Override
    public void initialize() {
        super.initialize();
        for (BaseConnector connector : connectorList) {
            try {
                connector.connect();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
     * @see net.jgf.core.service.BaseService#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        for (BaseConnector connector : connectorList) {
            connector.disconnect();
        }
    }
    
    

}
