
package net.jgf.jme.view.menu;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.view.display.DisplayItemsView;
import net.jgf.menu.MenuController;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

/**
 *
 */
@Configurable
public class MenuView extends BaseViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(MenuView.class);

	protected DisplayItemsView display;

	protected MenuController controller;

	protected MenuDisplayBuilder displayBuilder;

	// TODO: Add sound capabilities

	public MenuView() {
		controller = new MenuController();
		display = new DisplayItemsView();
		display.setId(this.getId() + "-DisplayItems");
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void load() {
		super.load();
		controller.reset();

		display.load();
		displayBuilder = new MenuDisplayBuilder();
	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#unload()
	 */
	@Override
	public void unload() {
		super.unload();
		display.unload();
		displayBuilder = null;
	}

	/**
	 * Scene geometry update.
	 */
	@Override
	public void update(float tpf) {

			super.update(tpf);

			// Check if we need to rebuild the display
			if (! controller.isViewValid()) {
				displayBuilder.build(display, controller);
				controller.setViewValid(true);
			}

			display.update(tpf);

	}


	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		super.render(tpf);
		display.render(tpf);

	}



	/**
	 *
	 * @see net.jgf.core.state.BaseState#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		display.activate();
	}

	/**
	 *
	 * @see net.jgf.core.state.BaseState#deactivate()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		display.deactivate();
	}

	/**
	 * @return the controller
	 */
	public MenuController getController() {
		return controller;
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);
		String initialMenuRef = config.getString(configPath + "/initialMenu/@ref");
		Jgf.getDirectory().register(controller, "initialMenu", initialMenuRef);
	}

	/**
	 * <p>Marks the current menu view as invalid. If the menu screen which currently visible is modified externally, the menu
	 * needs to be redrawn. Calling this method will ensure that the menu is redrawn
	 * during the next update cycle.</p>
	 */
	public void invalidateMenuView() {
		controller.setViewValid(false);
	}

}
