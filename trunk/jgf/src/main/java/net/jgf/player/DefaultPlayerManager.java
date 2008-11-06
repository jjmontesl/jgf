
package net.jgf.player;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.core.service.ServiceException;
import net.jgf.system.System;

import org.apache.log4j.Logger;

/**
 * The PlayerManage does not naturally restrict the amount of players or teams that may exist,
 * as that is a network issue. Other games could store hundreds of NPCs in several teams.
 */
// TODO: Use ArrayLists for this?
// TODO: This dual link implementation is dangerous. Store references to player only once.
// TODO: Clarify this classes, as they will be core classes (PlayerManaager, Player, Group)
// TODO: !!! Missing BasePlayerManager
@Configurable
public class DefaultPlayerManager extends BaseService implements PlayerManager {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DefaultPlayerManager.class);

	/**
	 * List of players
	 */
	protected PlayerGroup rootGroup;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		rootGroup = ConfigurableFactory.newFromConfig(config, configPath + "/player", PlayerGroup.class);
		System.getDirectory().addObject(rootGroup.getId(), rootGroup);

	}


	@Override
	public void dispose() {
		rootGroup = null;
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.service.BaseService#initialize()
	 */
	@Override
	public void initialize() {
		super.initialize();
		if (rootGroup == null) throw new ServiceException("No Player defined for " + this);
	}

	/* (non-Javadoc)
	 * @see net.jgf.player.PlayerManager#getRootGroup()
	 */
	public PlayerGroup getRootGroup() {
		return rootGroup;
	}

	/**
	 * @param rootState the rootState to set
	 */
	public void setRootGroup(PlayerGroup rootGroup) {
		this.rootGroup = rootGroup;
	}

}
