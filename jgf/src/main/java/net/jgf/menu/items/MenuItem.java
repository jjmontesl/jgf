
package net.jgf.menu.items;

import net.jgf.core.component.Component;
import net.jgf.menu.MenuController;

/**
 *
 * @author jjmontes
 * @version 1.0
 */
public interface MenuItem extends Component {

	void perform(MenuController controller);

	boolean isNavigable();

	String getKey();

}