
package net.jgf.settings;

import java.util.Collection;

/**
 * Common interface to access settings.
 * <p>Settings implementors provide ways to read different settings. Settings are accessed
 * by name (i.e. "render.gamma" or "network.port").</p>
 */
public interface Settings {

	public Setting<?> getSetting(String key);

	public <T extends Setting<?>> T getSetting(String key, Class<T> expectedClass);
	
	public Collection<Setting<?>> getSettings();
	
	/**
	 * Registers an object that will be updated on setting changes.
	 * @param target the object that will be notified of changes.
	 * @param field the field that will be set (target object needs to have a public setter for this field).
	 * @param id the id of the setting for which changes will be notified.
	 */
	public void register(Object target, String field, String id);

}

