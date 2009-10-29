package net.jgf.settings;

import java.util.Hashtable;
import java.util.Set;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.system.Application;

import org.apache.log4j.Logger;

@Configurable
public class SettingsManager extends BaseService implements Settings {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Application.class);

	/**
	 * StandardSettingsManager table initial capacity
	 */
	private static final int INITIAL_SETTINGS_CAPACITY = 40;

	/**
	 * Map of stored settings
	 */
	protected Hashtable<String, Setting> settings;


	
	public SettingsManager() {
		super();
		settings = new Hashtable<String, Setting>(INITIAL_SETTINGS_CAPACITY);
	}



	/* (non-Javadoc)
	 * @see net.jgf.core.component.BaseComponent#readConfig(net.jgf.config.Config, java.lang.String)
	 */
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);
		
		int index = 1;
		while (config.containsKey(configPath + "/settings/setting[" + index + "]/@name")) {
			
			Setting setting = ConfigurableFactory.newFromConfig(config, configPath + "/settings/setting[" + index + "]", Setting.class);
			addSetting(setting);
			
			index++;
		}
		
	}

	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#getSetting(java.lang.String)
	 */
	public Setting getSetting(String key) throws ConfigException {
		Setting item = settings.get(key);
		if (item == null) {
			throw new ConfigException ("Tried to retrieve undefined setting '" + key + "'");
		}
		return item;
	}

	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#containsKey(java.lang.String)
	 */
	public boolean containsKey(String key) {
		return settings.contains(key);
	}

	/**
	 * Adds an item to settings. It is not public because settings cannot be added
	 * in real time, all of them need to be defined in the configuration.
	 * @param item
	 * @throws ConfigException
	 */
	private void addSetting(Setting item) throws ConfigException {
		if (settings.containsKey(item.getName())) {
			throw new ConfigException ("Redefinition of settings is not allowed ('" + item.getName() + "')");
		}
		settings.put(item.getName(), item);
	}

	
	public void setValue(String key, String value) throws ConfigException {
		Setting setting = getSetting(key);
		setting.setValue(value);
	}


	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#getKeys()
	 */
	@Override
	public Set<String> getKeys() {
		return settings.keySet();

	}



	@Override
	public String getValue(String key) {
		Setting setting = getSetting(key);
		return setting.getValue();
	}




}