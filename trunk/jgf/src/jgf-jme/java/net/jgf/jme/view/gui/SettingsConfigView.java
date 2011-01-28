
package net.jgf.jme.view.gui;



import net.jgf.config.Config;
import net.jgf.config.Configurable;

import org.apache.log4j.Logger;

/**
 *
 */
@Configurable
public class SettingsConfigView extends NiftyGuiView {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SettingsConfigView.class);

	public SettingsConfigView() {
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void doLoad() {

		super.doLoad();

		// Add controls
		
	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		// Read settings
		
	}

}
