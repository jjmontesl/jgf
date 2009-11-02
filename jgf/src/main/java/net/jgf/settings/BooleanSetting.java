package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;

/**
 * Map of stored settings
 */
@Configurable
public class BooleanSetting extends Setting {

	Boolean value;

	public void setStringValue(String value) {
		this.value = Boolean.parseBoolean(value);
		manager.update(this.id, this.value);
	}
	
	public String getStringValue() {
		if (value == null) {
			setStringValue(this.getDefaultValue());
		}
		return Boolean.toString(value);
	}
	
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);
	}

	@Override
	public Object getValue() {
        if (value == null) {
            setStringValue(this.getDefaultValue());
        }
		return value;
	}
	
	
	
}
