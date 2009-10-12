
package net.jgf.jme.view.menu;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.menu.MenuController;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

/**
 *
 */
// TODO: Renderer should be customizable
@Configurable
public class DefaultMenuView extends BaseViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DefaultMenuView.class);

	protected MenuController controller;

	protected MenuLookAndFeel menuLaf;

	// TODO: Add sound capabilities
	
	// TODO: Add mouse input capabilities

	public DefaultMenuView() {
		controller = new MenuController();
		menuLaf = null;
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#input(float)
	 */
	@Override
	public void input(float tpf) {
		super.input(tpf);
		menuLaf.input(tpf);
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void load() {
		super.load();

		controller.setCurrentMenu(controller.getInitialMenu());
		controller.reset();
		menuLaf.load();
	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#unload()
	 */
	@Override
	public void unload() {
		super.unload();
		menuLaf.unload();
		menuLaf = null;
	}

	/**
	 * Scene geometry update.
	 */
	@Override
	public void update(float tpf) {

			super.update(tpf);

			menuLaf.update(tpf);

	}


	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		super.render(tpf);
		menuLaf.render(tpf);

	}



	/**
	 *
	 * @see net.jgf.core.state.BaseState#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		menuLaf.activate();
	}

	/**
	 *
	 * @see net.jgf.core.state.BaseState#deactivate()
	 */
	@Override
	public void deactivate() {
		menuLaf.deactivate();
		super.deactivate();
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
		
		setMenuLaf(ConfigurableFactory.newFromConfig(config, configPath + "/lookAndFeel", MenuLookAndFeel.class));
	}

	public MenuLookAndFeel getMenuLaf() {
		return menuLaf;
	}

	public void setMenuLaf(MenuLookAndFeel menuLaf) {
		this.menuLaf = menuLaf;
		this.menuLaf.setController(this.controller);
	}

	
	
}
