package net.jgf.settings;

import java.util.Hashtable;
import java.util.Set;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
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
	protected Hashtable<String, SettingsItem> settings;



	public SettingsManager() {
		super();
		settings = new Hashtable<String, SettingsItem>(INITIAL_SETTINGS_CAPACITY);
	}



	/* (non-Javadoc)
	 * @see net.jgf.core.component.BaseComponent#readConfig(net.jgf.config.Config, java.lang.String)
	 */
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);

	}



	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#getString(java.lang.String)
	 */
	public String getString(String key) throws ConfigException {
		Object res = getObject(key);
		if (! (res instanceof String)) {
			throw new ConfigException ("Cannot retrieve setting '" + key + "' as String");
		}
		return (String) res;
	}

	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#getInteger(java.lang.String)
	 */
	public int getInteger(String key) throws ConfigException {
		Object res = getObject(key);
		if (! (res instanceof Integer)) {
			throw new ConfigException ("Cannot retrieve setting '" + key + "' as Integer");
		}
		return ((Integer) res).intValue();
	}

	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String key) throws ConfigException {
		Object res = getObject(key);
		if (! (res instanceof Boolean)) {
			throw new ConfigException ("Cannot retrieve setting '" + key + "' as Boolean");
		}
		return ((Boolean) res).booleanValue();
	}


	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#getFloat(java.lang.String)
	 */
	public float getFloat(String key) throws ConfigException {
		Object res = getObject(key);
		if (! (res instanceof Float)) {
			throw new ConfigException ("Cannot retrieve setting '" + key + "' as String");
		}
		return ((Float) res).floatValue();
	}

	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#getObject(java.lang.String)
	 */
	public Object getObject(String key) throws ConfigException {
		return getSetting(key).getValue();
	}

	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#getSetting(java.lang.String)
	 */
	public SettingsItem getSetting(String key) throws ConfigException {
		SettingsItem item = settings.get(key);
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
	private void addSettingsItem(SettingsItem item) throws ConfigException {
		if (settings.containsKey(item.getKey())) {
			throw new ConfigException ("Overriding settings ('" + item.getKey() + "') is not allowed.");
		}
		settings.put(item.getKey(), item);
	}

	
	public void setValue(String key, String value) throws ConfigException {
		// TODO: Implement!
		throw new ConfigException("Cannot set values to this Settings (not implemented yet)");
	}


	/* (non-Javadoc)
	 * @see net.jgf.settings.Settings#getKeys()
	 */
	@Override
	public Set<String> getKeys() {
		return settings.keySet();

	}




}