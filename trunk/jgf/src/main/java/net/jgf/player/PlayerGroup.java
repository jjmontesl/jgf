package net.jgf.player;

public interface PlayerGroup extends Player {

	/**
	 * Adds a player to this team
	 */
	public void attachPlayer(Player player);

	/**
	 * Removes a player from this team
	 */
	public void dettachPlayer(Player player);

}