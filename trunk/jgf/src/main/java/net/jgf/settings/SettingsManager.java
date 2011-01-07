package net.jgf.settings;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

/**
 * <p>
 * </p>
 * 
 * @version 1.0
 * @author jjmontes
 */
@Configurable
public final class SettingsManager extends BaseService implements Settings {

    /**
     * Class logger
     */
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(SettingsManager.class);

    /**
     * StandardSettingsManager table initial capacity
     */
    private static final int INITIAL_SETTINGS_CAPACITY = 256;

    /**
     * Map of stored settings
     */
    protected Hashtable<String, Setting<?>> settings;

    public SettingsManager() {
        super();
        settings = new Hashtable<String, Setting<?>>(INITIAL_SETTINGS_CAPACITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.jgf.core.component.BaseComponent#readConfig(net.jgf.config.Config,
     * java.lang.String)
     */
    @Override
    public void readConfig(Config config, String configPath) {

        super.readConfig(config, configPath);

        List<Setting> settings = ConfigurableFactory.newListFromConfig(config, configPath
                + "/settings/setting", Setting.class);
        for (Setting setting : settings) {
            addSetting(setting);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.jgf.settings.Settings#getSetting(java.lang.String)
     */
    public Setting<?> getSetting(String key) throws ConfigException {
        Setting<?> item = settings.get(key);
        if (item == null) {
            throw new ConfigException("Tried to retrieve undefined setting '" + key + "'");
        }
        return item;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.jgf.settings.Settings#getSetting(java.lang.String)
     */
    @SuppressWarnings (value="unchecked") 
    public <T extends Setting<?>> T getSetting(String key, Class<T> expectedClass) {
        Setting<?> s = settings.get(key);
        T item  = null;
        item = (T) s;
        if (item == null) {
            throw new ConfigException("Tried to retrieve undefined setting '" + key + "'");
        }
        return item;
    }
    

    public boolean containsKey(String key) {
        return settings.containsKey(key);
    }

    /**
     * Adds an item to settings. It is not public because settings cannot be
     * added in real time, all of them need to be defined in the configuration.
     * 
     * @param setting
     * @throws ConfigException
     */
    private void addSetting(Setting<?> setting) throws ConfigException {
        if (settings.containsKey(setting.getId())) {
            throw new ConfigException("Redefinition of settings is not allowed ('" + setting.getId()
                    + "')");
        }
        setting.setManager(this);
        settings.put(setting.getId(), setting);
        Jgf.getDirectory().addObject(setting.getId(), setting);
    }

    @Override
    public Collection<Setting< ? >> getSettings() {
        Collection<Setting< ? >> res = settings.values();
        return res;
    }

}