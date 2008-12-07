package net.jgf.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.core.component.BaseComponent;

public abstract class BaseLoader<E> extends BaseComponent implements Loader<E> {

	protected Map<String, String> defaultProperties;

	public BaseLoader() {
		defaultProperties = new HashMap<String, String>();
	}

	protected void checkNullBase(E base) {
		if (base != null) {
			throw new ConfigException("Loader " + this + " cannot load onto a base element, it should receive null");
		}
	}

	public E load(E base, String... properties) {
		LoadProperties map = new LoadProperties(properties.length);
		for (String property : properties) {
			int pos = property.indexOf('=');
			if ((pos > property.length() - 2) || (pos < 1)) {
				throw new ConfigException("Invalid property (key=value) found when loading " + this + " from a list of properties");
			}
			String key = property.substring(0, pos);
			String value = property.substring(pos + 1);
			map.put(key, value);
		}
		return load(base, map);
	}

	protected void combineProperties(LoadProperties passedProperties) {
		for (Entry<String, String> entry : defaultProperties.entrySet()) {
			if (! passedProperties.containsKey(entry.getKey())) passedProperties.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		// Read default properties
		List<String> properties = config.getList(configPath + "/property/@name");
		for (String name : properties) {
			String value = config.getString(configPath + "/property[@name='" + name + "']/@value");
			defaultProperties.put(name, value);
		}

	}

}