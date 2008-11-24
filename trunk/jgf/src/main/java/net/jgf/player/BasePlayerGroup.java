
package net.jgf.player;

import java.util.ArrayList;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.ConfigurableFactory;
import net.jgf.system.Jgf;

/**
 */
public abstract class BasePlayerGroup extends BasePlayer implements PlayerGroup {


	protected ArrayList<Player> players;


	public BasePlayerGroup() {
		players = new ArrayList<Player>();
	}

	/**
	 *
	 * @see net.jgf.player.Group#attachPlayer(net.jgf.player.BasePlayer)
	 */
	// TODO: Document contract of association
	public void attachPlayer(Player player) {
		players.add(player);
		player.setParent(this);
	}

	/* (non-Javadoc)
	 * @see net.jgf.player.Group#removePlayer(net.jgf.player.BasePlayer)
	 */
	public void dettachPlayer(Player player) {
		players.remove(player);
		player.setParent(null);
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		List<Player> childPlayers= ConfigurableFactory.newListFromConfig(config, configPath + "/player", Player.class);
		for (Player player : childPlayers ) {
			this.attachPlayer(player);
			Jgf.getDirectory().addObject(player.getId(), player);
		}

	}

}
