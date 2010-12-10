package net.jgf.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.core.component.BaseComponent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public abstract class BaseLoader<E> extends BaseComponent implements Loader<E> {
    
    /**
     * Class logger
     */
    private static final Logger logger = Logger.getLogger(BaseLoader.class);

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
	    
	    logger.debug("Loader " + this + " loading over " + base);
	    
		LoadProperties map = new LoadProperties(properties.length);
		for (String property : properties) {
		    if (StringUtils.isNotBlank(property)) {
    			int pos = property.indexOf('=');
    			if ((pos > property.length() - 2) || (pos < 1)) {
    				throw new ConfigException("Invalid property (key=value) found when loading " + this + " from a list of properties: " + property);
    			}
    			String key = property.substring(0, pos);
    			String value = property.substring(pos + 1);
    			map.put(key, value);
		    }
		}
		return load(base, map);
	}

	public E load(E base) {
	    return load(base, new LoadProperties());
	}
	
	/**
	 * <p>Combines the set of properties passed with this loader's
	 * configuration properties, favouring the passed properties when
	 * there's a conflict.</p>
	 * <p>This method is used by loaders to mix their configuration properties 
	 * with the set of properties in the loading context, which allows
	 * chaining loaders while permitting overriding of properties.</p>
	 * @param passedProperties
	 */
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