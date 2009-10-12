package net.jgf.jme.view.menu;

import net.jgf.jme.view.display.DisplayItemsView;
import net.jgf.menu.MenuController;
import net.jgf.view.ViewState;

interface MenuLookAndFeel extends ViewState {

	public abstract Widget getCurrentWidget();

	public abstract MenuWidget getCurrentMenuWidget();

	public abstract void buildMenu();

	public abstract void clearMenu();
	
	public abstract void setController(MenuController menuController);
	
	public abstract DisplayItemsView getDisplay();
	
	public abstract MenuController getController();

}