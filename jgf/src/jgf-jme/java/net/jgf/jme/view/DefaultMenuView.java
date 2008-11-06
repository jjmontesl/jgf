
package net.jgf.jme.view;



import net.jgf.core.state.BaseState;

import org.apache.log4j.Logger;

/**
 * <p>This state shows the view menu.</p>
 */
public class DefaultMenuView extends BaseState {

	/**
	 * Logger for this class
	 */
	private final static Logger logger = Logger.getLogger(DefaultMenuView.class);

	/**
	 * A reference to the GUI system
	 */
	//protected GUI gui;

	/*
	protected Container mainMenu;

	protected Button startGameButton;

	protected Button connectButton;

	protected Button optionsButton;

	protected Button quitButton;
	*/



	/**
	 * Initializes the GUI portion for the Menu.
	 *
	 */
	protected void initGUI() {

		/*

		logger.info("Initializing Menu GUI");

		// Grab a display using an LWJGL binding
		disp = new org.fenggui.Display();
		input = new FengJMEInputHandler(disp);

		// Container
		mainMenu = new Container(new RowLayout(false));
		disp.addWidget(mainMenu);

		// Menu label
		Label label = new Label("Java Game Framework");
		label.getAppearance().setFont(gui.getMenuFont());
		label.getAppearance().setTextColor(Color.WHITE);
		label.getAppearance().setMargin(new Spacing(20, 0));
		label.setSizeToMinSize();
		mainMenu.addWidget(label);

		// Start game
		startGameButton = new Button("Start Game");
		startGameButton.getAppearance().setFont(gui.getMenuFont());
		startGameButton.getAppearance().setTextColor(Color.WHITE);
		startGameButton.setSizeToMinSize();
		startGameButton.addButtonPressedListener(this);
		mainMenu.addWidget(startGameButton);

		// Connect
		connectButton = new Button("Connect");
		connectButton.getAppearance().setFont(gui.getMenuFont());
		connectButton.getAppearance().setTextColor(Color.WHITE);
		connectButton.setSizeToMinSize();
		connectButton.addButtonPressedListener(this);
		mainMenu.addWidget(connectButton);

		// Options
		optionsButton = new Button("Options");
		optionsButton.getAppearance().setFont(gui.getMenuFont());
		optionsButton.getAppearance().setTextColor(Color.WHITE);
		optionsButton.setSizeToMinSize();
		optionsButton.addButtonPressedListener(this);
		mainMenu.addWidget(optionsButton);

		// Quit
		quitButton = new Button("Quit");
		quitButton.getAppearance().setFont(gui.getMenuFont());
		quitButton.getAppearance().setTextColor(Color.WHITE);
		quitButton.setSizeToMinSize();
		quitButton.addButtonPressedListener(this);
		mainMenu.addWidget(quitButton);

		mainMenu.layout();
		mainMenu.setSizeToMinSize();
		StaticLayout.center(mainMenu, disp);

		//MouseInput.get().setCursorVisible(true);

    // Update the display with the newly added components
    disp.layout();

    */

	}

	/**
	 * Scene geometry update.
	 */
	public void update(float tpf) {

		// Update child gamestates
		//gui.getRootNode().updateGeometricState(tpf, false);
		//super.update(tpf);

	}


	/**
	 * Draws the level.
	 */
	public void render(float tpf) {
		//DisplaySystem.getDisplaySystem().getRenderer().draw(gui.getRootNode());
	}


	/**
	 * Responses to button events
	 */
	/*
	public void buttonPressed(ButtonPressedEvent arg0) {

		IWidget sourceWidget = arg0.getSource();

		if (sourceWidget == startGameButton) {
			Commands.map("");
		}	else	if (sourceWidget == optionsButton) {
			disp.removeWidget(mainMenu);
		} else if (sourceWidget == quitButton) {
			Commands.quit();
		}

	}
	*/


}
