
package net.jgf.jme.view.display;

import net.jgf.core.component.BaseComponent;

import com.jme.scene.Node;

/**
 *
 * @author jjmontes
 * @version $Revision$
 */
public abstract class DisplayItem extends BaseComponent {

	public enum DisplayItemAlignment {

		Left,

		Center,

		Right,
		
		TopLeft,
		
		Top,
		
		TopRight,
		
		BottomLeft,
		
		Bottom,
		
		BottomRight

	}
	
	public abstract void refreshNode(Node display);

	public abstract void destroyNode(Node display);

}