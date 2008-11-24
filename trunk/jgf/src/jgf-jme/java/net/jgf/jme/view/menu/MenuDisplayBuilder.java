
package net.jgf.jme.view.menu;



import net.jgf.config.Configurable;
import net.jgf.core.service.ServiceException;
import net.jgf.jme.view.display.DisplayItemsView;
import net.jgf.jme.view.display.TextItem;
import net.jgf.menu.DefaultMenu;
import net.jgf.menu.MenuController;
import net.jgf.menu.items.MenuItem;
import net.jgf.menu.items.SeparatorMenuItem;
import net.jgf.menu.items.TextMenuItem;

import org.apache.log4j.Logger;

import com.jme.math.Vector3f;

/**
 *
 */
@Configurable
class MenuDisplayBuilder {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(MenuDisplayBuilder.class);

	protected float size = 0.09f;

	protected float spacing = size * 1.5f;

	public void build(DisplayItemsView display, MenuController controller) {

		float yPos = 0.5f;
		float xPos = 0.0f;

		DefaultMenu menu = controller.getCurrentMenu();

		display.clearItems();

		for (MenuItem item : menu.getItems()) {

			if (item instanceof TextMenuItem) {
				TextMenuItem textMenuItem = (TextMenuItem) item;
				TextItem textItem = new TextItem();
				textItem.setCenter(new Vector3f(xPos, yPos, 0));
				textItem.setText(textMenuItem.getText());
				textItem.setSize(size * textMenuItem.getSize());
				display.addItem(textItem);
				yPos -= spacing;
			}	else if (item instanceof SeparatorMenuItem) {
				yPos -= (spacing * 0.5f);
			} else {
				throw new ServiceException("The current MenuView is not able to draw a menu as it doesn't know how to draw item " + item );
			}

		}

	}

}
