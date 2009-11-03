
package net.jgf.player;

import net.jgf.config.Config;
import net.jgf.core.component.BaseComponent;


/**
 */
public abstract class BasePlayer extends BaseComponent implements Player {

	/**
	 * The player name
	 */
	protected String name;

	/**
	 * The team this player is in.
	 */
	protected PlayerGroup parent;




	/**
	 * @param name
	 */
	public BasePlayer(String name) {
		super();
		this.name = name;
	}



	public BasePlayer() {
		super();
	}



	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.name = config.getString(configPath + "/@name", this.getId());

	}

	/* (non-Javadoc)
	 * @see net.jgf.player.Player#getTeam()
	 */
	public PlayerGroup getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(PlayerGroup parent) {
		if (this.parent != null)	{
			if (this.parent != parent) {
				this.parent.dettachPlayer(this);
				this.parent = parent;
				this.parent.attachPlayer(this);
			}
		} else {
			this.parent = parent;
			this.parent.attachPlayer(this);
		}

	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(PlayerGroup team) {
		this.parent = team;
	}

	/* (non-Javadoc)
	 * @see net.jgf.player.Player#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see net.jgf.player.Player#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[id = " + this.getId() + "]";
	}

}
