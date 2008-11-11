
package net.jgf.jme.view.display;

import net.jgf.core.component.BaseComponent;

import com.jme.scene.Node;

/**
 *
 * @author jjmontes
 * @version $Revision$
 */
public abstract class DisplayItem extends BaseComponent {

	// TODO: Wrong! DisplayItems should draw themselves (load, activate, render: viewstates!)
	public abstract void load(Node display);

}