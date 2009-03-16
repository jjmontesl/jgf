
package net.jgf.jme.view.menu;



import net.jgf.jme.view.display.TextItem;
import net.jgf.jme.view.menu.DefaultMenuLookAndFeel.MenuRendererContext;
import net.jgf.menu.items.LabelMenuItem;
import net.jgf.menu.items.MenuItem;
import net.jgf.menu.items.TitleMenuItem;

import com.jme.math.Vector3f;

/**
 *
 */
// TODO: Renderer should be customizable
public class TextWidget extends Widget {

	TextItem textItem = null;

	@Override
	public void build(MenuItem item2, MenuRendererContext context) {

		super.build(item2, context);

		textItem = new TextItem();
		textItem.setCenter(new Vector3f(context.xPos, context.yPos, 0));


		if (laf.controller.getCurrentMenuItem() == item2) {
			textItem.setText("[" + item2.getKey() + "]");
		} else {
			textItem.setText(item2.getKey());
		}

		float sizeMult = 1.0f;
		if (item2 instanceof LabelMenuItem) sizeMult = 0.8f;
		if (item2 instanceof TitleMenuItem) sizeMult = 1.2f;
		textItem.setSize(context.size * sizeMult);

		context.laf.display.addItem(textItem);

		context.yPos -= context.size * sizeMult + context.spacing;

	}

	@Override
	public void destroy() {
		laf.display.removeItem(textItem);
		textItem = null;
	}

	@Override
	public void update(float tpf) {

		if (! isViewValid) {

			if (laf.controller.getCurrentMenuItem() == item) {
				textItem.setText("[" + item.getKey() + "]");
			} else {
				textItem.setText(item.getKey());
			}
			textItem.refreshNode(laf.display.getRootNode());

			isViewValid = true;
		}

	}


}
