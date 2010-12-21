
package net.jgf.jme.gui;



import java.io.IOException;
import java.net.URL;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.core.state.StateHelper;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.util.resource.ResourceLocatorTool;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.mapping.DefaultScreenMapping;
import de.lessvoid.nifty.jme.input.JmeInputSystem;
import de.lessvoid.nifty.jme.render.JmeRenderDevice;
import de.lessvoid.nifty.jme.sound.JmeSoundDevice;
import de.lessvoid.nifty.screen.NullScreen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 *
 */
@Configurable
public class NiftyGuiView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(NiftyGuiView.class);

	protected Nifty nifty; 
	
	protected String file = "gui/gui/sample.xml";
	
	protected float endAfter = 4.0f;
	
	protected float timeElapsed = 0;
	
	protected boolean closing = false;
	
	private class OnEndScreenNotify implements EndNotify {
        
	    NiftyGuiView view = null;
	    
	    public OnEndScreenNotify(NiftyGuiView view) {
	        this.view = view;
	    }
	    
	    @Override
        public void perform() {
            StateHelper.deactivateAndUnload(view);
        }
    };
	
	public NiftyGuiView() {
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
		
	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#unload()
	 */
	@Override
	public void unload() {
		nifty.exit();
	    super.unload();
	}

	
	
	@Override
    public void activate() {
        
        
        ScreenController controller = new JgfDefaultScreenController(this);
        
        URL screenUrl = ResourceLocatorTool.locateResource("config", file);
        if (screenUrl == null) {
            throw new ConfigException("Could not find NiftyGUI screen resource at: " + file);
        }
        logger.debug ("Loading NiftyGUI screen from " + screenUrl.toExternalForm());
        

        try {
            nifty.fromXml("niftygui-" + this.getId(), screenUrl.openStream(), "start", controller);
            
        } catch (IOException e) {
            throw new ConfigException("Could not read GUI resource: " + file, e);
        }
        
        this.timeElapsed = 0;
        this.closing = false;
        //this.nifty.gotoScreen("start");
        
        // FIXME: This a workaround to cause observers to be notified after activation. This should be
        // enforced by the framework (doActivate, doLoad?)
        super.activate();
        
    }

	
    /**
	 * Scene geometry update.
	 */
	@Override
	public void update(float tpf) {

			super.update(tpf);
			
			this.timeElapsed += tpf;
			if ((! this.closing) && (this.endAfter > 0)) {
    			if (this.timeElapsed > this.endAfter) {
    			    logger.debug("Automatically ending NiftyGUI screen " + this.getId() + " after " + this.endAfter + " seconds");
    			    nifty.getCurrentScreen().endScreen(new OnEndScreenNotify(this));		        
    			    this.closing = true;
    			}    
			}
			
	}


	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		super.render(tpf);
		
	    if (nifty.render(false))  {
	        if (this.isActive()) this.deactivate();
	    }
		
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.setFile(config.getString(configPath + "/file"));
		this.setEndAfter(config.getFloat(configPath + "/endAfter", 0));
		
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

    public float getEndAfter() {
        return endAfter;
    }

    public void setEndAfter(float endAfter) {
        this.endAfter = endAfter;
    }

    public Nifty getNifty() {
        return nifty;
    }

}
