package net.jgf.settings.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.core.service.BaseService;
import net.jgf.settings.Setting;
import net.jgf.settings.SettingHandler;
import net.jgf.settings.Settings;
import net.jgf.settings.StringSetting;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

/**
 * <p>PropertiesStorage provides storage capabilities for settings. It is able to
 * write and read settins to and from property files in the filesystem.</p>
 * 
 * @version 1.0
 * @author jjmontes
 */
@Configurable
public final class PropertiesStorage extends BaseService {

    // TODO: Add support to store only a subset of settings (i.e. profile based)?
    
    // TODO: Add support to choose a different file at write time?
    
    // TODO: Add support to store XML settings format
    
    /**
     * Class logger
     */
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(PropertiesStorage.class);

    protected Settings settings;
    
    protected Set<String> excludes = new HashSet<String>();
    
    protected SettingHandler<String> path = new SettingHandler<String>(StringSetting.class);
    
    public PropertiesStorage() {
        super();
        path.setValue("$USER_HOME/" + Jgf.getApp().getKey() + ".properties");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.jgf.core.component.BaseComponent#readConfig(net.jgf.config.Config,
     * java.lang.String)
     */
    @Override
    public void readConfig(Config config, String configPath) {

        super.readConfig(config, configPath);
        
        
        path.readValue(config.getString(configPath + "/path", path.getValue()));
        
        String settingsRef = config.getString(configPath + "/settings/@ref");
        Jgf.getDirectory().register(this, "settings", settingsRef);
        
        List<String> excludeList = config.getList(configPath + "/excludes/exclude/@pattern");
        for (String exclude : excludeList) {
            excludes.add(exclude);
        }

    }

    private boolean filterSetting(String settingId) {
        boolean passes = true;
        for (String exclude: excludes) {
            if (settingId.matches(exclude)) {
                passes = false;
                break;
            }
        }
        return passes;
    }
    
    @Override
    public void dispose() {
        this.writeSettings();
        super.dispose();
    }

    @Override
    public void initialize() {
        super.initialize();
        this.readSettings();
    }
    
    private String expandPath(String path) {
        String rpath = path;
        rpath = rpath.replace("$USER_HOME", System.getProperty("user.home"));
        rpath = rpath.replace("$USER_DIR", System.getProperty("user.dir"));
        return rpath;
    }

    public void readSettings() {
        
        String epath = expandPath(this.path.getValue());
        logger.info("Reading settings from file: " + epath);
        
        Properties properties = new Properties();
        try {
            Reader reader = new FileReader(epath);
            properties.load(reader);
        } catch (FileNotFoundException e) {
            logger.warn("File not found when trying to read settings from " + epath);
        } catch (IOException e) {
            logger.error("Error while reading settings from " + epath, e);
        }
        
        for (Setting< ? > setting : settings.getSettings()) {
            if (filterSetting(setting.getId())) {
                String propValue = properties.getProperty(setting.getId());
                if (propValue != null) {
                    try {
                        setting.readValue(propValue);
                    } catch (ConfigException e) {
                        // If read value cannot be parsed:
                        setting.reset();
                    }
                }
            }
        }
    }
    
    public void writeSettings() {
        
        // Resolve the properties path
        
        // TODO: Write settings in alphabetical order
        
        String epath = expandPath(this.path.getValue());
        logger.info("Writing settings to file: " + epath);
        
        Properties properties = new Properties();
        for (Setting<?> setting : settings.getSettings()) { 
            if (filterSetting(setting.getId())) {
                properties.put(setting.getId(), setting.toString());
            }
        }
        
        File file = new File(epath);
        try {
            Writer writer = new FileWriter(file);
            properties.store(writer, 
                    "JGF - Java Game Framework - Settings Configuration file\n"
                    + "Game: " + Jgf.getApp().getName() + " " + Jgf.getApp().getVersion() + "\n"
                    + "Originally written to: " + epath);
            writer.close();
        } catch (IOException e) {
            logger.error("Could not write settings to " + epath, e);
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getPath() {
        return path.getValue();
    }

    public void setPath(String path) {
        this.path.readValue(path);
    }
    
    
}