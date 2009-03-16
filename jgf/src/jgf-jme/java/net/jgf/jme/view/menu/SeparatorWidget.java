
package net.jgf.jme.view.menu;



import net.jgf.jme.view.menu.DefaultMenuLookAndFeel.MenuRendererContext;
import net.jgf.menu.items.MenuItem;

/**
 *
 */
// TODO: Renderer should be customizable
public class SeparatorWidget extends Widget {

	@Override
	public void build(MenuItem item, MenuRendererContext context) {

		super.build(item, context);

		context.yPos -= (context.spacing * 0.75f);

	}

	@Override
	public void destroy() {
		// Nothing to do
	}

	@Override
	public void update(float tpf) {
		// Nothing to do
	}

}
