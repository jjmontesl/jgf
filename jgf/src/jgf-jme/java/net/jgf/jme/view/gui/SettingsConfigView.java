
package net.jgf.jme.view.gui;



import java.util.ArrayList;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.jme.settings.KeySetting;
import net.jgf.settings.Setting;
import net.jgf.settings.SettingsManager;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.input.controls.controller.ControlChangeListener;

import de.lessvoid.nifty.controls.button.controller.ButtonControl;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.elements.Element;

/**
 *
 */
@Configurable
public class SettingsConfigView extends NiftyGuiView {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SettingsConfigView.class);

	public enum SettingsItemType {
	    SETTING,
	    SEPARATOR
	}
	
	public class SettingsItem {
	    
	    SettingsItemType type = SettingsItemType.SETTING;
	    
	    Setting<?> setting;

        public SettingsItemType getType() {
            return type;
        }

        public void setType(SettingsItemType type) {
            this.type = type;
        }

        public Setting<?> getSetting() {
            return setting;
        }

        public void setSetting(Setting<?> setting) {
            this.setting = setting;
        }
	    
	}
	
	private List<SettingsItem> items = new ArrayList<SettingsItem>();
	   
    public SettingsConfigView() {
        
    }

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void doLoad() {

		super.doLoad();

	}
	
	


	@Override
    public void doActivate() {
        super.doActivate();
        
        // Add controls
        for (SettingsItem item : items) {
            drawSettingsItem(item);
        }
    }
	
	private void drawSettingsItem(SettingsItem item) {
	    
	    if (item.getType() == SettingsItemType.SEPARATOR) {
	        drawSeparator(item);
	    } else if (item.getType() == SettingsItemType.SETTING) {
	        drawSetting(item);
	    }
       
	}
	
	private void drawSeparator(SettingsItem item) {
	    Element element = this.nifty.getCurrentScreen().findElementByName("settings");
        PanelCreator panelCreator = new PanelCreator();
        panelCreator.setHeight("8px");
        panelCreator.create(nifty, nifty.getCurrentScreen(), element);
	}
	
	private void drawSetting(SettingsItem item) {
	    Element element = this.nifty.getCurrentScreen().findElementByName("settings");
	    
	    PanelCreator panelCreator = new PanelCreator();
	    panelCreator.setChildLayout("horizontal");
	    Element panel = panelCreator.create(nifty, nifty.getCurrentScreen(), element);
	    
	    LabelCreator labelCreator = new LabelCreator(item.getSetting().getLabel());
        labelCreator.create(nifty, nifty.getCurrentScreen(), panel);
        
        PanelCreator panel2Creator = new PanelCreator();
        panel2Creator.setWidth("40px");
        panel2Creator.create(nifty, nifty.getCurrentScreen(), panel);
        
        if (item.getSetting() instanceof KeySetting) {
            drawKeySettingControl(item, panel);
        } else {
            throw new ConfigException("Invalid setting type for SettingsConfigView: " + item.getSetting()); 
        }
        
	}
	
	private void drawKeySettingControl(SettingsItem item, Element parent) {
	    LabelCreator labelCreator = new LabelCreator(item.getSetting().toString());
        labelCreator.create(nifty, nifty.getCurrentScreen(), parent);
	}

    /**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		// Read settings
		List<String> settingNames = config.getList(configPath + "/items/item/@setting");
        for (String settingName : settingNames) {
            SettingsItem settingsItem = new SettingsItem();
            String type = config.getString(configPath + "/items/item[@setting='" + settingName + "']/@type");
            if ("separator".equals(type)) {
                settingsItem.type = SettingsItemType.SEPARATOR;
            } else if ("setting".equals(type)) {
                settingsItem.type = SettingsItemType.SETTING;
                Jgf.getDirectory().register(settingsItem, "setting", settingName);
            } else {
                throw new ConfigException("Unknown setting item type '" + type + "' when reading config for " + this);
            }
            items.add(settingsItem);
        }
		
	}

}
