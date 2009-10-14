package net.jgf.settings;



/**
 * The settings engine.
 * This loads all settings definitions from config.
 * Also loads and instantiates some components that will can be used to provide setters and getters
 * (setters and getters are called through reflection).
 * All settings are provided as strings, although the set metod can throw an exception if
 * the parameter is invalid.
 * Users are not expected to subclass this class (or I may just fail to see the reason now).
 * @author jjmontes
 */
public class SettingsItem {

	String key;

	Object value;

	public SettingsItem(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}


}