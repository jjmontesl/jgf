package net.jgf.jme.gui;

import java.util.concurrent.Callable;

import net.jgf.config.ConfigException;
import net.jgf.jme.view.gui.NiftyGuiView;
import net.jgf.logic.action.LogicAction;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * This class is intended to be used or extended.
 */
public class JgfScreenController implements ScreenController {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(JgfScreenController.class);
    
    protected Nifty nifty;
    
    protected Screen screen;
    
    protected NiftyGuiView view;
    
    private class ActionTask implements Callable<Object> {
        private LogicAction action;
        
        public ActionTask(LogicAction action) {
            this.action = action;
        }
        public Object call() {
            action.perform(null);
            return null;
        }
    }
    
    private class QuitNotify implements EndNotify {
        
        @Override
        public void perform() {
            quit();
        }
    }
    
    private class ActionNotify implements EndNotify {
        
        String actionId = null; 
        
        
        
        JgfScreenController controller = null;
        
        public ActionNotify(JgfScreenController controller, String actionId) {
            this.controller = controller;
            this.actionId = actionId;
        }
        
        @Override
        public void perform() {
            controller.view.deactivate();
            controller.view.unload();
            doAction(actionId);
            
            //LogicAction action = Jgf.getDirectory().getObjectAs(actionId, LogicAction.class);
            //action.perform(null);
        }
    };
    
    public JgfScreenController(NiftyGuiView view) {
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
    
    public void endScreenAndQuit() {
        screen.endScreen(new QuitNotify());
    }
    
    public void doAction(String id) {
        
        LogicAction action = Jgf.getDirectory().getObjectAs(id, LogicAction.class);
        
        if (Jgf.getApp().isDebug()) {
            if (action == null) {
                throw new ConfigException("Cannot run action from " + this
                        + " (no action set)");
            }

        }
        
        //action.perform(null);
        Callable<Object> performTask = new ActionTask(action);
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).enqueue(performTask);
    }
    
    public void quit() {
        logger.info("Quitting application");
        Jgf.getApp().dispose();
    }
  
}
