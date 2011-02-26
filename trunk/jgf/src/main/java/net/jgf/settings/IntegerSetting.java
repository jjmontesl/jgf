package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;

/**
 * Map of stored settings
 */
@Configurable
public class IntegerSetting extends Setting<Integer> {

    @Override
    public Integer parseValue(String value) {
        // TODO: Hack to allow commas instead of periods for decimal separator
        return Integer.parseInt(value);
    }

    public String toString() {
        return this.getValue().toString();
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

}
