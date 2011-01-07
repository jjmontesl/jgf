package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;

/**
 * Map of stored settings
 */
@Configurable
public class FloatSetting extends Setting<Float> {

    @Override
    public Float parseValue(String value) {
        // TODO: Hack to allow commas instead of periods for decimal separator
        return Float.parseFloat(value.replace(',', '.'));
    }

    public String toString() {
        return String.format("%.2f", this.getValue());
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

}
