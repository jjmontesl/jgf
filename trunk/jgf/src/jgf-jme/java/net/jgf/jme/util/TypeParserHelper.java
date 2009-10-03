
package net.jgf.jme.util;

import java.lang.reflect.Field;

import net.jgf.config.ConfigException;

import org.apache.log4j.Logger;

import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;


/**
 * <p>Provides helper methods to read JME types from configuration</p>.
 *
 * @author jjmontes
 */

public final class TypeParserHelper  {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(TypeParserHelper.class);

	public static int valueOfKeyInput(String keyInput)  {
		int result;
		try {
			Field field = KeyInput.class.getField(keyInput);
			result = field.getInt(null);
		} catch (SecurityException e) {
			throw new ConfigException("Could not find KeyInput '" + keyInput + "' when parsing KeyInput value", e);
		} catch (NoSuchFieldException e) {
			throw new ConfigException("Unknown key name '" + keyInput + "' when parsing a KeyInput value");
		} catch (IllegalArgumentException e) {
			throw new ConfigException("Could not access KeyInput '" + keyInput + "' parsing KeyInput value", e);
		} catch (IllegalAccessException e) {
			throw new ConfigException("Could not access KeyInput '" + keyInput + "' parsing KeyInput value", e);
		}
		return result;
	}

	public static Vector3f valueOfVector3f(String vector3f) {
		Vector3f result = new Vector3f();

		String separator = (vector3f.indexOf(',') >= 0 ? "," : "\\ +");

		String[] components = vector3f.split(separator);
		if (components.length != 3) {
			throw new ConfigException("Vector3f format exception (a vector should have 3 components but " + components.length + " were found");
		}

		result.x = Float.valueOf(components[0].trim());
		result.y = Float.valueOf(components[1].trim());
		result.z = Float.valueOf(components[2].trim());

		return result;
	}

	public static ColorRGBA valueOfColorRGBA(String vector3f) {

		ColorRGBA result = new ColorRGBA();

		String separator = (vector3f.indexOf(',') >= 0 ? "," : "\\ +");

		String[] components = vector3f.split(separator);
		if ((components.length < 3) || (components.length > 4)) {
			throw new ConfigException("ColorRGBA format exception (a color should have 3 or 4 components)");
		}

		result.r = Float.valueOf(components[0].trim());
		result.g = Float.valueOf(components[1].trim());
		result.b = Float.valueOf(components[2].trim());
		if (components.length > 3) result.a = Float.valueOf(components[3].trim());

		return result;
	}



}
