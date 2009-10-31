package net.jgf.loader;

import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;

import org.apache.commons.beanutils.BeanUtils;



/**
 *
 */
// TODO: Organize conversion and loading strategy
@Configurable
public class AttributeLoader<T> extends BaseLoader<T> {

	@Override
	public T load(T base, LoadProperties properties) throws LoaderException {

		combineProperties(properties);
		
		for (Entry<String,String> entry : properties.entrySet()) {
			
			// Properties are prefixed with AttributeLoader
			if (entry.getKey().startsWith("AttributeLoader")) {
				String propertyName = entry.getKey().substring(15);
				try {
					BeanUtils.setProperty(base, propertyName, entry.getValue());
				} catch (IllegalAccessException e) {
					throw new ConfigException("Could not set property '" + propertyName + "' on " + base + " at loader " + this);
				} catch (InvocationTargetException e) {
					throw new ConfigException("Could not set property '" + propertyName + "' on " + base + " at loader " + this);
				}
			}
		}
		
		return base;

	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}

}
