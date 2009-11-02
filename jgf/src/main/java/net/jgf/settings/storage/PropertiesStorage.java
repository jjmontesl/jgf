package net.jgf.settings.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.service.BaseService;
import net.jgf.settings.Setting;
import net.jgf.settings.Settings;
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

    /**
     * Class logger
     */
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(PropertiesStorage.class);

    protected Settings settings;
    
    protected String path;
    
    public PropertiesStorage() {
        super();
        path = "$USER_HOME/" + Jgf.getApp().getName() + ".properties";
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
        
        path = config.getString(configPath + "/path", path);
        
        String settingsRef = config.getString(configPath + "/settings/@ref");
        Jgf.getDirectory().register(this, "settings", settingsRef);

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
        
        String epath = expandPath(this.path);
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
            String propValue = properties.getProperty(setting.getId());
            if (propValue != null) {
                setting.setStringValue(propValue);
            }
        }
    }
    
    public void writeSettings() {
        
        // Resolve the properties path
        
        String epath = expandPath(this.path);
        logger.info("Writing settings to file: " + epath);
        
        Properties properties = new Properties();
        for (Setting setting : settings.getSettings()) { 
            properties.put(setting.getId(), setting.getStringValue());
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
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
}