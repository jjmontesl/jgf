/*
 * General Gaming Framework
 * $Id$
 */

package net.jgf.jme.config;

import net.jgf.config.Config;
import net.jgf.jme.util.TypeParserHelper;

import org.apache.log4j.Logger;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;


/**
 * <p>Provides helper methods to read JME types from configuration</p>.
 *
 * @author jjmontes
 * @version $Revision$
 */

public final class JmeConfigHelper  {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(JmeConfigHelper.class);

	public static int getKeyInput(Config config, String path, int defaultKeyInput)  {
		int result = defaultKeyInput;
		if (config.containsKey(path)) result = JmeConfigHelper.getKeyInput(config, path);
		return result;
	}

	public static int getKeyInput(Config config, String path)  {
		String keyInput = config.getString(path);
		int result = TypeParserHelper.valueOfKeyInput(keyInput);
		return result;
	}

	public static Vector3f getVector3f(Config config, String path, Vector3f defaultVector) {
		Vector3f result = defaultVector;
		if (config.containsKey(path)) result = JmeConfigHelper.getVector3f(config, path);
		return result;
	}

	public static Vector3f getVector3f(Config config, String path)  {
		String vector3f = config.getString(path);
		Vector3f result = TypeParserHelper.valueOfVector3f(vector3f);
		return result;
	}

	public static ColorRGBA getColor(Config config, String path) {
		String color = config.getString(path);
		ColorRGBA result = TypeParserHelper.valueOfColorRGBA(color);
		return result;
	}

	public static ColorRGBA getColor(Config config, String path, ColorRGBA defaultColor) {
		ColorRGBA result = defaultColor;
		if (config.containsKey(path)) result = JmeConfigHelper.getColor(config, path);
		return result;
	}

}
