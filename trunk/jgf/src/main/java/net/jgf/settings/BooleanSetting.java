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

	public void setValue(String value) {
		this.value = Boolean.parseBoolean(value);
	}
	
	public String getValue() {
		if (value == null) {
			setValue(this.getDefaultValue());
		}
		return Boolean.toString(value);
	}
	
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);
	}
	
}
