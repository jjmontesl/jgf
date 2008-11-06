
package net.jgf.menu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.system.System;

import org.apache.log4j.Logger;

/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class DefaultMenuSystem extends BaseService {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DefaultMenuSystem.class);

	protected Set<DefaultMenu> menus;


	public DefaultMenuSystem() {
		super();
		menus = new HashSet<DefaultMenu>();
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		List<DefaultMenu> tMenus = ConfigurableFactory.newListFromConfig(config, configPath + "/menu", DefaultMenu.class);
		for (DefaultMenu menu : tMenus) {
			menus.add(menu);
			System.getDirectory().addObject(menu.getId(), menu);
		}

	}



}