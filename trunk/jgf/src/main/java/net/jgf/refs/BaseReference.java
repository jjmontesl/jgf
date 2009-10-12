
package net.jgf.refs;

import net.jgf.config.Config;

import org.apache.log4j.Logger;


/**
 */
// TODO: Should this be a component and have ID, in case someone wants to define this in the XML?
public class BaseReference implements Reference {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseReference.class);

	/**
	 * Map identifier (its short name).
	 */
	protected String name;

	/* (non-Javadoc)
	 * @see net.jgf.scene.Scene#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Configures this object from Config.
	 */
	public void readConfig(Config config, String configPath) {
		this.name = config.getString(configPath + "/name");
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
