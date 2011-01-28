package net.jgf.loader;

import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.logic.action.control.ControllerAction;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;



/**
 *
 */
// TODO: Organize conversion and loading strategy
@Configurable
public class AttributeLoader<T> extends BaseLoader<T> {


    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(AttributeLoader.class);
    
	@Override
	public T load(T base, LoadProperties properties) throws LoaderException {

		combineProperties(properties);
		
		for (Entry<String,String> entry : properties.entrySet()) {
			
			// Properties are prefixed with AttributeLoader
			if (entry.getKey().startsWith("AttributeLoader")) {
				String propertyName = entry.getKey().substring(16);
				try {
				    if (logger.isTraceEnabled()) {
				        logger.trace("Setting attribute " + propertyName + " to '" + entry.getValue() + "' on object " + base.toString());
				    }
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
