
package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.core.component.BaseComponent;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
public abstract class BaseMenuItem extends BaseComponent implements MenuItem {

	protected String key;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		key = config.getString(configPath + "/key", null);

	}

	/**
	 * @return the text
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the text to set
	 */
	public void setKey(String key) {
		this.key = key;
	}




}