
package net.jgf.menu;

import java.util.ArrayList;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.component.BaseComponent;
import net.jgf.menu.items.MenuItem;
import net.jgf.system.System;

import org.apache.log4j.Logger;

/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class DefaultMenu extends BaseComponent {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DefaultMenu.class);

	protected List<MenuItem> items;


	public DefaultMenu() {
		super();
		items = new ArrayList<MenuItem>();
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		List<MenuItem> tItems = ConfigurableFactory.newListFromConfig(config, configPath + "/item", MenuItem.class);
		for (MenuItem item: tItems) {
			items.add(item);
			System.getDirectory().addObject(item.getId(), item);
		}

	}



}