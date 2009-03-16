
package net.jgf.menu.items;

import net.jgf.config.Configurable;
import net.jgf.menu.MenuController;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class LabelMenuItem extends BaseMenuItem {

	@Override
	public boolean isNavigable() {
	  return false;
	}



	@Override
	public void perform(MenuController controller) {
		// Nothing to do
	}

}