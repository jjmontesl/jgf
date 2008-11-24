
package net.jgf.menu.items;

import net.jgf.core.component.Component;

/**
 *
 * @author jjmontes
 * @version $Revision$
 */
public interface MenuItem extends Component {

	public void perform();

	public boolean isNavigable();

}