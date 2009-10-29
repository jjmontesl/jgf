package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;

/**
 * Map of stored settings
 */
@Configurable
public abstract class Setting {

	String name;

	String label;
	
	String defaultValue;
	

	public Setting() {
		super();
	}
	
	public Setting(String name, String label, String defaultValue) {
		super();
		
		this.name = name;
		this.label = label;
		this.setDefaultValue(defaultValue);
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public abstract void setValue(String value);
	
	public abstract String getValue();
	
	
	public void readConfig(Config config, String configPath) {
		
		this.setName(config.getString(configPath + "/@name"));
		this.setLabel(config.getString(configPath + "/@label"));
		this.setDefaultValue(config.getString(configPath + "/@default"));
		
	}
	
}
