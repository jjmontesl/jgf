package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;

/**
 * Map of stored settings
 */
@Configurable
public class StringSetting extends Setting<String> {

    String value;

    public void setStringValue(String value) {
        this.value = value;
        manager.update(this.getId(), this.value);
    }

    public String getStringValue() {
        if (value == null) {
            setStringValue(this.getDefaultValue());
        }
        return value;
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

    @Override
    public String getValue() {
        if (value == null) {
            setStringValue(this.getDefaultValue());
        }
        return value;
    }

}
