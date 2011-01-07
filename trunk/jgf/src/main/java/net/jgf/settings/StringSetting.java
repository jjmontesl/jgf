package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;

/**
 * Map of stored settings
 */
@Configurable
public class StringSetting extends Setting<String> {

    @Override
    public String parseValue(String value) {
        return value;
    }

    public String toString() {
        return this.getValue();
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

}
