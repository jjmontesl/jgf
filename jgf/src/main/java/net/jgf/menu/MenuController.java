
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

	protected Menu initialMenu;

	protected Menu currentMenu;

	protected int currentIndex;

	public void nextItem() {
		changeItem(+1);
	}

	public void previousItem() {
		changeItem(-1);
	}

	private void changeItem(int offset) {

		int initialIndex = currentIndex;
		boolean stepChanged;

		do {
			stepChanged = false;
			int previousIndex = currentIndex;
			currentIndex += offset;;
			if (currentIndex < 0) currentIndex = 0;
			if (currentIndex >= currentMenu.getItems().size()) currentIndex = currentMenu.getItems().size() - 1;
			if (previousIndex != currentIndex) stepChanged = true;
		} while (stepChanged && (! currentMenu.getItems().get(currentIndex).isNavigable()));

		if (! currentMenu.getItems().get(currentIndex).isNavigable()) currentIndex = initialIndex;

	}

	public void useCurrentItem() {
		getCurrentMenuItem().perform(this);
	}

	public void reset() {

		// Move the initial index to the first navigable option
		currentIndex = 0;
		if (! currentMenu.getItems().get(currentIndex).isNavigable()) nextItem();
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
	public Menu getInitialMenu() {
		return initialMenu;
	}

	/**
	 * @param initialMenu the initialMenu to set
	 */
	public void setInitialMenu(Menu initialMenu) {
		this.initialMenu = initialMenu;
	}

	/**
	 * @return the currentMenu
	 */
	public Menu getCurrentMenu() {
		return currentMenu;
	}

	/**
	 * @param currentMenu the currentMenu to set
	 */
	public void setCurrentMenu(Menu currentMenu) {
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

}