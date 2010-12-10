package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;

/**
 * Map of stored settings
 */
@Configurable
public class FloatSetting extends Setting<Float> {

    Float value;

    public void setStringValue(String value) {
        // TODO: Hack to allow commas instead of periods for decimal separator
        this.value = Float.parseFloat(value.replace(',', '.'));
        manager.update(this.getId(), this.value);
    }

    public String getStringValue() {
        if (value == null) {
            setStringValue(this.getDefaultValue());
        }
        return String.format("%.2f", value);
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

    @Override
    public Float getValue() {
        if (value == null) {
            setStringValue(this.getDefaultValue());
        }
        return value;
    }

}
