
package net.jgf.settings;

import java.util.Set;

import net.jgf.config.ConfigException;

/**
 * Common interface to access settings.
 * <p>The settings component is a core facility of JGF. For example, it is used by the framework
 * to decide whether the application should be started in dedicated server mode.</p>
 * <p>Settings implementors provide ways to read different settings. Settings are accessed
 * by name (i.e. "render.gamma" or "network.port").</p>
 * <p>Methods of this interface throw a ConfigException if user tries to access
 * an inexistent property. Use {@link #containsKey(String)} if you need to know whether
 * a given property exists.</p>
 */
public interface Settings {

	public String getString(String key) throws ConfigException;

	public int getInteger(String key) throws ConfigException;

	public boolean getBoolean(String key) throws ConfigException;

	public float getFloat(String key) throws ConfigException;

	public Object getObject(String key) throws ConfigException;

	public Set<String> getKeys();

	public boolean containsKey(String key);

}

