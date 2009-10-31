/**
 * $Id: System.java,v 1.1 2008/01/08 16:35:49 jjmontes Exp $
 * Java Game Framework
 */

package net.jgf.util.system;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.jgf.core.naming.Directory;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

/**
 *
 * Thread safe??
 * @author jjmontes
 */
public class NamingUtils {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(NamingUtils.class);


	/**
	 * Generates a report about the directory
	 */
	public static String directoryReport () {

		Directory directory = Jgf.getDirectory();
		StringBuilder report = new StringBuilder();

		Set<String> keys = directory.getIds();
		SortedSet<String> sortedKeys = new TreeSet<String>(keys);

		for (String key : sortedKeys) {
			Object object = directory.getObjectAs(key, Object.class);
			report.append("  ").append(key).append("\n    ").append(object.toString()).append("\n");
		}
		report.append("Directory ").append(directory).append("\n");
		report.append("Size: ").append(directory.getSize()).append("\n");
		report.append("Peak size: ").append(directory.getPeakSize()).append("\n");

		return report.toString();
	}

}
