package net.jgf.jme.settings;

import java.lang.reflect.Field;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.jme.util.TypeParserHelper;
import net.jgf.settings.Setting;

import com.jme.input.KeyInput;

/**
 * Map of stored settings
 */
@Configurable
public class KeySetting extends Setting<Integer> {

    public Integer parseValue(String value) {
        return TypeParserHelper.valueOfKeyInput(value);
    }

    public String toString() {

        // TODO: Too much iterations and not too efficient: shall cache values or build a map
        
        String stringValue = "";
        Field[] fields = KeyInput.class.getFields();
        for (Field field : fields) {
            try {
                if (field.getType() == int.class) {
                    if (field.getInt(null) == this.getValue().intValue())
                        stringValue = field.getName();
                }
            } catch (IllegalAccessException e) {
                throw new ConfigException("Could not access KeyInput '" + field.getName()
                        + "' parsing KeyInput value", e);
            }
        }

        return stringValue;
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

}
