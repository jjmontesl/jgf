
package net.jgf.jme.view.display;



import java.util.ArrayList;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

/**
 *
 * <p>Note that the FaderViewNode fades to black (or other color) everything that has already been
 * drawn. Therefore this node cannot be used to fade only certain elements on the screen if something
 * else has already been rendered (in order to do so, those other elements would provide their
 * own alpha blending faders). Think of this node as a "full screen fader".</p>
 * <p>This node by default forces render queue processing, so fading is processed
 * immediately and other geometry can be drawn over the faded stencil buffer.</p>
 */
@Configurable
public class DisplayItemsView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(DisplayItemsView.class);

	protected Node rootNode;

	protected List<DisplayItem> displayItems;

	public DisplayItemsView() {
		displayItems = new ArrayList<DisplayItem>();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void load() {

		rootNode = new Node("display-" + this.getId());

		super.load();

		for (DisplayItem item : displayItems) {
			item.refreshNode(this.rootNode);
		}

	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#unload()
	 */
	@Override
	public void unload() {
		super.unload();
		rootNode.detachAllChildren();
	}

	/**
	 * Scene geometry update.
	 */
	@Override
	public void update(float tpf) {

			super.update(tpf);
			rootNode.updateGeometricState(tpf, true);

	}


	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		super.render(tpf);

		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);

	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		List<DisplayItem> list = ConfigurableFactory.newListFromConfig(config, configPath + "/item", DisplayItem.class);
		for (DisplayItem item : list) {
			Jgf.getDirectory().addObject(item.getId(), item);
			this.addItem(item);
		}

	}

	public void addItem(DisplayItem item) {
		displayItems.add(item);
		if (this.isLoaded()) item.refreshNode(this.rootNode);
	}

	public void clearItems() {
		// TODO: Clear items, textures...
		this.rootNode.detachAllChildren();
		displayItems.clear();
	}

	public void removeItem(DisplayItem item) {
		item.destroyNode(this.rootNode);
		displayItems.remove(item);

	}

	/**
	 * @return the rootNode
	 */
	public Node getRootNode() {
		return rootNode;
	}



}
