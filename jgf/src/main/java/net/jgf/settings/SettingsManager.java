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
 * The SettingsManager holds and provides access to application settings. These
 * are commonly used to hold data such as difficulty level, key bindings, user
 * name and preferences, video rendering settings, etc.
 * </p>
 * <p>
 * JGF settings manager is String friendly, therefore all values can be read and
 * written as a String. This tries to use cases where settings may need to be
 * passed as strings (console access, writing settings to configuration files,
 * sending settings through network).
 * </p>
 * <p>
 * In-memory settings storage is done in custom classes, which provide access to
 * typed values.
 * </p>
 * <p>
 * <b>Note:</b> This class has not been designed to be extended or replaced.
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

    protected SettingsRegistry settingsRegistry;
    
    public SettingsManager() {
        super();
        settings = new Hashtable<String, Setting<?>>(INITIAL_SETTINGS_CAPACITY);
        settingsRegistry = new SettingsRegistry(this);
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
        settingsRegistry.update(setting.getId(), setting.getValue());
    }

    public void setStringValue(String key, String value) throws ConfigException {
        Setting<?> setting = getSetting(key);
        setting.setStringValue(value);
    }

    public String getStringValue(String key) {
        Setting< ? > setting = getSetting(key);
        return setting.getStringValue();
    }

    @Override
    public Collection<Setting< ? >> getSettings() {
        Collection<Setting< ? >> res = settings.values();
        return res;
    }

    @Override
    public void register(Object target, String field, String id) {
        settingsRegistry.register(target, field, id);
    }
    
    void update(String id, Object value) {
        settingsRegistry.update(id, value);
    }

}