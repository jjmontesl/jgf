
package net.jgf.settings;

import java.util.Enumeration;

/**
 * Common interface to access settings.
 * <p>Settings implementors provide ways to read different settings. Settings are accessed
 * by name (i.e. "render.gamma" or "network.port").</p>
 */
public interface Settings {

	public Setting<?> getSetting(String key);

	public Enumeration<Setting<?>> getSettings();

}

