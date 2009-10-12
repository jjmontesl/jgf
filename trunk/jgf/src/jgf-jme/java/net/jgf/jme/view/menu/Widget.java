package net.jgf.jme.view.menu;

import net.jgf.jme.view.menu.DefaultMenuLookAndFeel.MenuRendererContext;
import net.jgf.menu.items.MenuItem;

import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;

/**
 *
 */
// TODO: Renderer should be customizable
public abstract class Widget {

	protected MenuItem item;

	protected boolean isViewValid = false;

	protected MenuLookAndFeel laf;

	public void build(MenuItem item, MenuRendererContext context) {
		this.item = item;
		this.laf = context.laf;
	}

	public abstract void destroy();

	public void handleInput(InputActionEvent evt) {

		this.isViewValid = false;

		if (evt.getTriggerIndex() == KeyInput.KEY_UP) {
			laf.getController().previousItem();
		}
		if (evt.getTriggerIndex() == KeyInput.KEY_DOWN) {
			laf.getController().nextItem();
		}
		if (evt.getTriggerIndex() == KeyInput.KEY_SPACE) {
			laf.getController().useCurrentItem();
		}

		if (laf.getCurrentWidget() != null) laf.getCurrentWidget().setViewValid(false);

	}

	/**
	 * @return the isViewValid
	 */
	public boolean isViewValid() {
		return isViewValid;
	}

	/**
	 * the isViewValid to set
	 */
	public void setViewValid(boolean isViewValid) {
		this.isViewValid = isViewValid;
	}



	public abstract void update(float tpf);

}
