
package net.jgf.jme.gui;



import java.io.IOException;
import java.net.URL;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.util.resource.ResourceLocatorTool;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.jme.input.JmeInputSystem;
import de.lessvoid.nifty.jme.render.JmeRenderDevice;
import de.lessvoid.nifty.jme.sound.JmeSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 *
 */
@Configurable
public class NiftyView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(NiftyView.class);

	protected Nifty nifty; 
	
	protected String file = "gui/gui/sample.xml";
	
	public NiftyView() {
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void load() {

		super.load();

		nifty = new Nifty(
		        new JmeRenderDevice(),
		        new JmeSoundDevice(),
		        new JmeInputSystem(),
		        new TimeProvider());
		
		URL screenUrl = ResourceLocatorTool.locateResource("config", file);
		if (screenUrl == null) {
		    throw new ConfigException("Could not find NiftyGUI screen resource at: " + file);
		}
		logger.debug ("Loading NiftyGUI screen from " + screenUrl.toExternalForm());
		
		try {
		    nifty.fromXml("niftygui-" + this.getId(), screenUrl.openStream(), "start");
		    
		} catch (IOException e) {
		    throw new ConfigException("Could not read GUI resource: " + file, e);
		}

	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#unload()
	 */
	@Override
	public void unload() {
		super.unload();
	}

	/**
	 * Scene geometry update.
	 */
	@Override
	public void update(float tpf) {

			super.update(tpf);

	}


	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		super.render(tpf);
		
		if (nifty == null) {
		    return;
		}

		 // forward keyboard events to nifty
/*
		while (Keyboard.next()) {
	        nifty.keyEvent(
	          Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState());
	      }
	*/
	  
	      // render nifty
	      //int mouseX = Mouse.getX();
	      //int mouseY = Display.getDisplayMode().getHeight() - Mouse.getY();
		
		boolean finished = false;
		
		int mouseX = 0;
		int mouseY = 0;
	      if (nifty.render(true)) /*, mouseX, mouseY, false /* Mouse.isButtonDown(0) ))*/ {
	        finished = true;
	      }

		
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.setFile(config.getString(configPath + "/file"));
		
		/*
		List<DisplayItem> list = ConfigurableFactory.newListFromConfig(config, configPath + "/item", DisplayItem.class);
		for (DisplayItem item : list) {
			Jgf.getDirectory().addObject(item.getId(), item);
			this.addItem(item);
		}
		*/

	}

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }




}
