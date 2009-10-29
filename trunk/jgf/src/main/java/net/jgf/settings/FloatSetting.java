package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;

/**
 * Map of stored settings
 */
@Configurable
public class FloatSetting extends Setting {

	Float value;

	public void setValue(String value) {
		this.value = Float.parseFloat(value);
	}
	
	public String getValue() {
		if (value == null) {
			setValue(this.getDefaultValue());
		}
		return String.format("%.2f", value);
	}
	
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);
	}
	
}
