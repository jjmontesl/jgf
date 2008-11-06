
package net.jgf.scene;

import net.jgf.core.component.Component;


/**
 */
public interface Scene extends Component {

	/**
	 * @return Returns the name.
	 */
	public String getName();

	/**
	 * @return Returns the title.
	 */
	public String getTitle();

	/**
	 * Updates the level (making all calculations needed that
	 * result in the next level state)
	 */
	public void update(float tpf);

	/**
	 * Cleans up resources used by this Scene
	 */
	public void dispose();

  public void setId(String id);

}
