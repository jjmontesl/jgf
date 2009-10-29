
package net.jgf.settings;

import java.util.Set;

import net.jgf.config.ConfigException;

/**
 * Common interface to access settings.
 * <p>Settings implementors provide ways to read different settings. Settings are accessed
 * by name (i.e. "render.gamma" or "network.port").</p>
 */
public interface Settings {

	public String getValue(String key);

	public void setValue(String key, String value);

	public Set<String> getKeys();

	public boolean containsKey(String key);

}

