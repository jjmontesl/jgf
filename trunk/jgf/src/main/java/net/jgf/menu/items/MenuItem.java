
package net.jgf.menu.items;

import net.jgf.core.component.Component;
import net.jgf.menu.MenuController;

/**
 *
 * @author jjmontes
 * @version $Revision$
 */
public interface MenuItem extends Component {

	public void perform(MenuController controller);

	public boolean isNavigable();

	public String getKey();

}