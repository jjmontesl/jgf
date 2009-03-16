
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
	// TODO: Check carefully: also makes sense to prepare a node... why draw themselves?
	// TODO: They could change, but also we could use: load/activate... maybe that's better
	public abstract void refreshNode(Node display);

	public abstract void destroyNode(Node display);

}