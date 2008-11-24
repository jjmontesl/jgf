
package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.config.Configurable;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class TextMenuItem extends BaseMenuItem {


	protected String text;

	protected float size = 1.0f;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		text = config.getString(configPath + "/text");
		size = config.getFloat(configPath + "/size", size);

	}

	@Override
	public boolean isNavigable() {
	  return false;
	}

	@Override
	public void perform() {
		// Nothing to do
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the size
	 */
	public float getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(float size) {
		this.size = size;
	}



}