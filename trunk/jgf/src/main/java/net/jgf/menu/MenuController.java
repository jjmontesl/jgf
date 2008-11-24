
package net.jgf.menu;


import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.component.BaseComponent;
import net.jgf.menu.items.MenuItem;

/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class MenuController extends BaseComponent {

	protected DefaultMenu initialMenu;

	protected DefaultMenu currentMenu;

	protected int currentIndex;

	protected boolean viewValid;

	public void nextItem() {
		// TODO: Skip separators...
		currentIndex++;
		viewValid = false;
		if (currentIndex < 0) currentIndex = 0;
		if (currentIndex >= currentMenu.getItems().size()) currentIndex = currentMenu.getItems().size() - 1;
	}

	public void previousItem() {
		currentIndex--;
		viewValid = false;
		if (currentIndex < 0) currentIndex = 0;
		if (currentIndex >= currentMenu.getItems().size()) currentIndex = currentMenu.getItems().size() - 1;
	}

	public void activateCurrentItem() {
		getCurrentMenuItem().perform();
		viewValid = false;
	}

	public void jumpTo() {
		viewValid = false;
	}

	public void jumpBack() {
		viewValid = false;
	}

	public void reset() {
		currentMenu = initialMenu;
		currentIndex = 0; // TODO: Move to first navigable, move back on perform if no navigable available
		viewValid = false;
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}

	public MenuItem getCurrentMenuItem() {
		return currentMenu.getItems().get(currentIndex);
	}

	/**
	 * @return the initialMenu
	 */
	public DefaultMenu getInitialMenu() {
		return initialMenu;
	}

	/**
	 * @param initialMenu the initialMenu to set
	 */
	public void setInitialMenu(DefaultMenu initialMenu) {
		this.initialMenu = initialMenu;
	}

	/**
	 * @return the currentMenu
	 */
	public DefaultMenu getCurrentMenu() {
		return currentMenu;
	}

	/**
	 * @param currentMenu the currentMenu to set
	 */
	public void setCurrentMenu(DefaultMenu currentMenu) {
		this.currentMenu = currentMenu;
	}

	/**
	 * @return the currentIndex
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * @param currentIndex the currentIndex to set
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * @return the viewValid
	 */
	public boolean isViewValid() {
		return viewValid;
	}

	/**
	 * @param viewValid the viewValid to set
	 */
	public void setViewValid(boolean viewValid) {
		this.viewValid = viewValid;
	}



}