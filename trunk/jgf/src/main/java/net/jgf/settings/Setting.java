package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.component.BaseComponent;

/**
 * Map of stored settings
 */
@Configurable
public abstract class Setting<T> extends BaseComponent {

	String label;
	
	String defaultValue;
	
	SettingsManager manager;

	public Setting() {
		super();
	}
	
	public Setting(String id, String label, String defaultValue) {
		super(id);
		this.label = label;
		this.setDefaultValue(defaultValue);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public abstract void setStringValue(String value);
	
	public abstract String getStringValue();
	
	public abstract T getValue();
	
	void setManager(SettingsManager manager) {
	    this.manager = manager;
	}
	
	public SettingsManager getManager() {
        return manager;
    }

    public void readConfig(Config config, String configPath) {
		
		super.readConfig(config, configPath);
		this.setLabel(config.getString(configPath + "/@label"));
		this.setDefaultValue(config.getString(configPath + "/@default"));
		
	}
    
    public void reset() {
        this.setStringValue(this.getDefaultValue());
    }
	
}
