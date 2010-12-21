package net.jgf.jme.gui;

import net.jgf.config.ConfigException;
import net.jgf.core.state.StateHelper;
import net.jgf.logic.action.LogicAction;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * This class is intended to be used or extended.
 */
public class JgfDefaultScreenController implements ScreenController {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(JgfDefaultScreenController.class);
    
    protected Nifty nifty;
    
    protected Screen screen;
    
    protected NiftyGuiView view;
    
    private class ActionNotify implements EndNotify {
        
        String actionId = null;
        
        JgfDefaultScreenController controller = null;
        
        public ActionNotify(JgfDefaultScreenController controller, String actionId) {
            this.controller = controller;
            this.actionId = actionId;
        }
        
        @Override
        public void perform() {
            StateHelper.deactivateAndUnload(controller.view);
            doAction(actionId);
        }
    };
    
    public JgfDefaultScreenController(NiftyGuiView view) {
        super();
        this.view = view;
    }

    /**
     */
    public void bind(final Nifty nifty, final Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    /**
     * on start screen.
     */
    public void onStartScreen() {
    }

    /**
     * on end screen.
     */
    public void onEndScreen() {
    }

    public void gotoScreen(String id) {
      nifty.gotoScreen(id);
    }

    public void endScreenAndDoAction(String id) {
        screen.endScreen(new ActionNotify(this, id));
    }
    
    public void doAction(String id) {
        
        LogicAction action = Jgf.getDirectory().getObjectAs(id, LogicAction.class);
        
        if (Jgf.getApp().isDebug()) {
            if (action == null) {
                throw new ConfigException("Cannot run action from " + this
                        + " (no action set)");
            }

        }

        action.perform(null);
    }
    
    public void quit() {
        logger.info("Quitting application");
        Jgf.getApp().finalize();
    }
  
}
