
package net.jgf.jme.view.gui;



import java.io.IOException;
import java.net.URL;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.core.IllegalStateException;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.util.resource.ResourceLocatorTool;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.jme.input.JmeInputSystem;
import de.lessvoid.nifty.jme.render.JmeRenderDevice;
import de.lessvoid.nifty.jme.sound.JmeSoundDevice;
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
	
	protected String file;
	
	protected float endAfter;
	
	protected float timeElapsed = 0;
	
	protected boolean closing = false;
	
	private class OnEndScreenNotify implements EndNotify {
        
	    NiftyGuiView view = null;
	    
	    public OnEndScreenNotify(NiftyGuiView view) {
	        this.view = view;
	    }
	    
	    @Override
        public void perform() {
            view.deactivate();
            view.unload();
        }
    };
	
	public NiftyGuiView() {
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void doLoad() {

		super.doLoad();

	}

	private void initNifty() {
	    
	    if (nifty != null) {
            throw new IllegalStateException ("Nifty instance is already created at load time");
        }
        
        nifty = new Nifty(
                new JmeRenderDevice(),
                new JmeSoundDevice(),
                new JmeInputSystem(),
                new TimeProvider());
        
        ScreenController controller = new JgfScreenController(this);

        URL screenUrl = ResourceLocatorTool.locateResource("config", file);
        if (screenUrl == null) {
            throw new ConfigException("Could not find NiftyGUI screen resource at: " + file);
        }
        logger.debug("Loading NiftyGUI screen from " + screenUrl.toExternalForm());

        try {
            nifty.fromXml("niftygui-" + this.getId(), screenUrl.openStream(), "start", controller);

        } catch (IOException e) {
            throw new ConfigException("Could not read GUI resource: " + file, e);
        }

        this.timeElapsed = 0;
        this.closing = false;
        
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#unload()
	 */
	@Override
	public void doUnload() {
		if (! ((nifty.getCurrentScreen() == null) || (nifty.getCurrentScreen().isNull())) ) {
		    nifty.exit();
		}
		nifty = null;
	    super.doUnload();
	}

	
	

    @Override
    public void doDeactivate() {
        super.doDeactivate();
    }

    @Override
    public void doActivate() {
        
        super.doActivate();
        
        closing = false;
        
        if (this.nifty == null) initNifty();
        
        //this.nifty.resetEvents();
        
        //this.nifty.gotoScreen(this.nifty.getCurrentScreen().getScreenId());
        //this.nifty.gotoScreen("start");
    }

    /**
	 * Scene geometry update.
	 */
	@Override
	public void doUpdate(float tpf) {

			super.doUpdate(tpf);
			
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
	public void doRender(float tpf) {

		super.doRender(tpf);
		
	    if (nifty.render(false))  {
	        if (this.isActive()) {
	            this.deactivate();
	            this.unload();
	        }
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
