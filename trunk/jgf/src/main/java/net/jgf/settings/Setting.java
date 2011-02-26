package net.jgf.settings;

import java.util.ArrayList;
import java.util.HashSet;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.component.BaseComponent;

/**
 * Map of stored settings
 */
@Configurable
public abstract class Setting<T> extends BaseComponent {

	private String label;
	
	private T defaultValue;
	
	private volatile T value;
	
	private SettingsManager manager;
	
	private HashSet<SettingObserver> observers;
	
	public Setting() {
		super();
	}
	
	public Setting(String id, String label, T defaultValue) {
		super(id);
		this.label = label;
		this.setDefaultValue(defaultValue);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public abstract T parseValue(String value);
	
	public abstract String toString();

    public synchronized T getValue() {
        if (this.value == null) this.reset();
        return this.value;
    }
	
	void setManager(SettingsManager manager) {
	    this.manager = manager;
	}
	
	public Settings getManager() {
        return manager;
    }

    public void readConfig(Config config, String configPath) {
		
		super.readConfig(config, configPath);
		this.setLabel(config.getString(configPath + "/@label"));
		this.setDefaultValue(this.parseValue(config.getString(configPath + "/@default")));
		this.reset();
		
	}
    
    public void reset() {
        this.setValue(this.getDefaultValue());
    }

    public void readValue(String value) {
        this.setValue(this.parseValue(value));
    }
    
    public synchronized void setValue(T value) {
        this.value = value;
        this.updateObservers();
    }
    
    public synchronized void addObserver(SettingObserver observer) {
        if (observers == null) {
            observers = new HashSet<SettingObserver>();
        }
        observers.add(observer);
    }
    
    public synchronized void removeObserver(SettingObserver observer) {
        if (observers == null) {
            observers = new HashSet<SettingObserver>();
        }
        observers.remove(observer);
    }
    
    public synchronized void clearObservers() {
        if (observers == null) {
            observers = new HashSet<SettingObserver>();
        } else {
            observers.clear();
        }
    }
    
    private void updateObservers() {
        if (observers != null) {
            for (SettingObserver listener : observers) {
                listener.onChange(this);
            }
        }
    }
}
