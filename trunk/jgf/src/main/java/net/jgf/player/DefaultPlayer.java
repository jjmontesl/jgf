
package net.jgf.player;

import net.jgf.config.Config;
import net.jgf.config.Configurable;


/**
 */
@Configurable
public class DefaultPlayer extends BasePlayer {

	private int points;

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}

}
