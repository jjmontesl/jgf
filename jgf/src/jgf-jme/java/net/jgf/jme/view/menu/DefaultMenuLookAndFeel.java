
package net.jgf.jme.view.menu;



import java.util.Hashtable;
import java.util.Map;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.core.JgfRuntimeException;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.jme.scene.util.TextQuadUtils;
import net.jgf.jme.view.display.DisplayItemsView;
import net.jgf.jme.view.display.DisplayItem.DisplayItemAlignment;
import net.jgf.menu.Menu;
import net.jgf.menu.MenuController;
import net.jgf.menu.items.ActionMenuItem;
import net.jgf.menu.items.LabelMenuItem;
import net.jgf.menu.items.MenuItem;
import net.jgf.menu.items.ScreenLinkMenuItem;
import net.jgf.menu.items.SeparatorMenuItem;
import net.jgf.menu.items.TitleMenuItem;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;

/**
 * <p>This class also handles input as needed.</p>
 * <p>Class is final. Not designed for inheritance.</p>
 */
@Configurable
public final class DefaultMenuLookAndFeel extends BaseViewState implements MenuLookAndFeel {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(DefaultMenuLookAndFeel.class);

	/**
	 * List of Menu Widgets. Changes over time. The map key is the id of the associated MenuItem.
	 */
	protected Map<String, Widget> widgets;
	
	protected DisplayItemsView display;

	protected MenuController controller;

	protected Map<Class<?>, Class<?>> widgetTypeMap;

	protected InputHandler inputHandler;

	protected MenuRendererContext context;

	protected String font = TextQuadUtils.DEFAULT_FONT;
	protected DisplayItemAlignment align = DisplayItemAlignment.Center; 
	protected float size = 0.09f;
	protected float spacing = size * 0.5f;
	protected Vector3f origin = new Vector3f();
	
	public static final class MenuRendererContext {

		protected MenuLookAndFeel laf;

		public float yPos;
		public float xPos;
		
	}


	/**
	 * Key action
	 */
	public class KeyInputAction extends InputAction {

		public void performAction(InputActionEvent evt) {

			//logger.trace("Key pressed (index=" + evt.getTriggerIndex() + ",time=" + evt.getTime() + ",press=" + evt.getTriggerPressed() + ")");

			if (evt.getTriggerPressed() == true) {
				if (getCurrentWidget() != null) getCurrentWidget().handleInput(evt);
			}

		}

	}


	
	
	public MenuController getController() {
		return controller;
	}

	public void setController(MenuController controller) {
		this.controller = controller;
	}

	
	
	public DisplayItemsView getDisplay() {
		return display;
	}

	public DefaultMenuLookAndFeel() {

		display = new DisplayItemsView();
		display.setId(this.toString() + "/DefaultMenuLookAndFeel-DisplayItems");

		widgets = new Hashtable<String, Widget>();
		widgetTypeMap = new Hashtable<Class<?>, Class<?>>();

		// Load the Widget Type Map
		// TODO: Should this be configurable? Autoregistering?
		// TODO: This requires an entry for every MenuItem type. Ideally, supertypes would be supported automatically and the most concrete class would be selected
		widgetTypeMap.put(SeparatorMenuItem.class, SeparatorWidget.class);
		widgetTypeMap.put(LabelMenuItem.class, TextWidget.class);
		widgetTypeMap.put(TitleMenuItem.class, TextWidget.class);
		widgetTypeMap.put(ScreenLinkMenuItem.class, TextWidget.class);
		widgetTypeMap.put(ActionMenuItem.class, TextWidget.class);

	}

	/* (non-Javadoc)
	 * @see net.jgf.jme.view.menu.MenuLookAndFeel#getCurrentWidget()
	 */
	public Widget getCurrentWidget() {
		String id = controller.getCurrentMenuItem().getId();
		Widget widget = widgets.get(id);

		return widget;
	}

	/* (non-Javadoc)
	 * @see net.jgf.jme.view.menu.MenuLookAndFeel#getCurrentMenuWidget()
	 */
	public MenuWidget getCurrentMenuWidget() {

		String id = controller.getCurrentMenu().getId();
		MenuWidget menuWidget = (MenuWidget) widgets.get(id);

		return menuWidget;
	}

	/* (non-Javadoc)
	 * @see net.jgf.jme.view.menu.MenuLookAndFeel#buildMenu()
	 */
	public void buildMenu() {

		Menu menu = controller.getCurrentMenu();

		if (menu == null) {
			throw new JgfRuntimeException("Can't build menu: no current menu set for MenuController " + controller.getId());
		}

		context = new MenuRendererContext();
		context.laf = this;
		context.xPos = origin.x;
		context.yPos = origin.y;

		MenuWidget menuWidget = new MenuWidget();
		widgets.put(menu.getId(), menuWidget);

		// TODO: These should probably be built and contained by the MenuWidget!!
		for (MenuItem item : menu.getItems()) {

			Widget widget = buildWidget(item, context);
			if (widget != null) widgets.put(item.getId(), widget);

		}

		menuWidget.setViewValid(true);

	}

	/* (non-Javadoc)
	 * @see net.jgf.jme.view.menu.MenuLookAndFeel#clearMenu()
	 */
	public void clearMenu() {

		for (Widget widget : widgets.values()) {
			widget.destroy();
		}
		widgets.clear();

	}

	public void load() {
		display.load();

		inputHandler = new InputHandler();
		inputHandler.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);
	}

	public void unload() {
		display.unload();
		inputHandler.removeAllActions();
		inputHandler = null;
	}

	public void update(float tpf) {

		// Check if widgets need to be created
		MenuWidget menuWidget = getCurrentMenuWidget();
		if ((menuWidget == null) || (! menuWidget.isViewValid())) {
			logger.debug("Cleaning and rebuilding menu");
			clearMenu();
			buildMenu();
		}


		// Update
		for (Widget widget : widgets.values()) {
			widget.update(tpf);
		}
		display.update(tpf);
	}

	public void render(float tpf) {

		display.render(tpf);

	}

	/**
	 * This maps MenuItems to their respective
	 */
	private Widget buildWidget(MenuItem item, MenuRendererContext context) {

		// Cast is safe as the map of type should be granted to be correct
		// TODO: This requires an entry for every MenuItem type. Ideally, supertypes would be supported automatically and the most concrete class would be selected
		@SuppressWarnings("unchecked")
		Class<Widget> widgetClass = (Class<Widget>) widgetTypeMap.get(item.getClass());

		if (widgetClass == null) {
			throw new ConfigException("Can't create widget: no widget class associated to MenuItem of type " + item.getClass().getCanonicalName());
		}

		Widget widget = null;
		try {
			widget = widgetClass.newInstance();
		} catch (InstantiationException e) {
			throw new ConfigException("Could not create menu widget of type " + widgetClass.getSimpleName() + " for MenuItem " + item.getId(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigException("Could not create menu widget of type " + widgetClass.getSimpleName() + " for MenuItem " + item.getId(), e);
		}

		widget.build(item, context);
		return widget;

	}

	
	
	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public DisplayItemAlignment getAlign() {
		return align;
	}

	public void setAlign(DisplayItemAlignment align) {
		this.align = align;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getSpacing() {
		return spacing;
	}

	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		setFont(config.getString(configPath + "/font", getFont()));
		setSize(config.getFloat(configPath + "/size", getSize()));
		setAlign(DisplayItemAlignment.valueOf(config.getString(configPath + "/align", getAlign().toString())));
		setOrigin(JmeConfigHelper.getVector3f(config, configPath + "/origin", origin));
	}

	public Vector3f getOrigin() {
		return origin;
	}

	public void setOrigin(Vector3f origin) {
		this.origin = origin;
	}

	public void activate() {
		// TODO: Build on activation (destroy on deactivation)??????
		display.activate();
	}



	public void deactivate() {
		display.deactivate();
		clearMenu();
	}

	public void input(float tpf) {
		inputHandler.update(tpf);
	}

}
