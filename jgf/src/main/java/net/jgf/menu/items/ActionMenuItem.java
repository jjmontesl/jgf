package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.logic.action.LogicAction;
import net.jgf.menu.MenuController;
import net.jgf.system.Jgf;

/**
 * 
 * @author jjmontes
 */
@Configurable
public class ActionMenuItem extends BaseMenuItem {

    protected LogicAction action;

    /**
     * Configures this object from Config.
     */
    @Override
    public void readConfig(Config config, String configPath) {

        super.readConfig(config, configPath);

        Jgf.getDirectory().register(this, "action",
                config.getString(configPath + "/action/@ref"));

    }

    /* (non-Javadoc)
     * @see net.jgf.menu.items.MenuItem#isNavigable()
     */
    @Override
    public boolean isNavigable() {
        return true;
    }

    @Override
    public void perform(MenuController controller) {

        if (Jgf.getApp().isDebug()) {
            if (action == null) {
                throw new ConfigException("Cannot run action from " + this
                        + " (no action set)");
            }

        }

        action.perform(null);
    }

    /**
     * @return the action
     */
    public LogicAction getAction() {
        return action;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(LogicAction action) {
        this.action = action;
    }

}