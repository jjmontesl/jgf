package net.jgf.player;

import net.jgf.core.component.Component;

public interface Player extends Component {

	/**
	 * @return the team
	 */
	public PlayerGroup getParent();

	/**
	 * @return the team
	 */
	public void setParent(PlayerGroup player);

	/**
	 *
	 */
	public String getName();

	/**
	 * Sets the player name
	 */
	public void setName(String name);

	/**
	 * @return the id
	 */
	public String getId();

}