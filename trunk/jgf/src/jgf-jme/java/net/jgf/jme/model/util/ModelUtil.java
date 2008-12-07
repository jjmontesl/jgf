
package net.jgf.jme.model.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import net.jgf.config.ConfigException;
import net.jgf.jme.model.ModelException;

import org.apache.log4j.Logger;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.collada.ColladaImporter;
import com.jmex.model.converters.AseToJme;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.MilkToJme;
import com.jmex.model.converters.ObjToJme;

// TODO: Use ResourceLocator to find models (DONE: REVIEW, saving, etc...)
// TODO: Separate utils and the loader in different classes
// FIXME: Caching seems not to be working after refactoring?
public class ModelUtil {

	private static final Logger logger = Logger.getLogger(ModelUtil.class);

	/**
	 *  This method opens a model in various format evaluating the extension.
	 *  In case the same resource can be found in jbin format, and that resource is not older than
	 *  the original file, then it is loaded instead.
	 *  Otherwise the model is loaded and saved in jbin format for later user.
	 */
	public static Node convertModel (String resourceName) throws ModelException {

		logger.debug("Loading model from resource '" + resourceName + "'");

		Node loadedModel = null;
		FormatConverter formatConverter = null;

		String originalExtension = resourceName.substring(resourceName.lastIndexOf(".") + 1, resourceName.length());

		// TODO: This is wrong. It should work if the original file is not present
		URL originalURL = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_MODEL, resourceName);
		if (originalURL == null) throw new ConfigException("Couldn't find resource " + resourceName + " when loading a model");

		File originalFile;
		try {
			originalFile = new File(originalURL.toURI());
		} catch (URISyntaxException e) {
			throw new ModelException ("Could not load resource '" + resourceName + "'", e);
		}

		String convertedFilename = originalFile.getName().substring(0, originalFile.getName().lastIndexOf(".") + 1)	+ "jbin";
		File convertedFile = new File(originalFile.getParentFile(), convertedFilename);

    // Resource locator
    URI texturesUri = originalFile.getParentFile().toURI();
		SimpleResourceLocator locator = new SimpleResourceLocator(texturesUri);
    ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);

		boolean doConvert = false;

		if (! "jbin".equals(originalExtension)) {

			// Check to see if the model has changed since last conversion
			boolean forceConvert = false;
			if (originalFile.exists() && convertedFile.exists()) {
				if (originalFile.lastModified() != convertedFile.lastModified()) {
					forceConvert = true;
				}
			}

			if (! (convertedFile.exists())||(forceConvert)) doConvert = true;

		}

		// Verify the presence of the jbin model
		if (doConvert) {

			logger.debug("Converting model from resource '" + resourceName + "'");

			// Evaluate the format
			if (originalExtension.equals("3ds")) {
				formatConverter = new MaxToJme();
			}
			if (originalExtension.equals("md2")) {
				formatConverter = new Md2ToJme();
			}
			if (originalExtension.equals("md3")) {
				formatConverter = new Md3ToJme();
			}
			if (originalExtension.equals("ms3d")) {
				formatConverter = new MilkToJme();
			}
			if (originalExtension.equals("ase")) {
				formatConverter = new AseToJme();
			}
			if (originalExtension.equals("obj")) {
				formatConverter = new ObjToJme();
				File matlib = new File(originalFile.getParentFile(), "fir.mtl");
				try {
					formatConverter.setProperty("mtllib", matlib.toURL());
				} catch (MalformedURLException e) {
					throw new ModelException ("Could not load material library for obj from '" + matlib.getAbsolutePath() + "'", e);
				}
			}

			//formatConverter.setProperty("mtllib", convertedURL);

			// Do the conversion
			try {

				if (originalExtension.equals("dae")) {

						InputStream modelStream = null;
						modelStream = originalURL.openStream();
		        ColladaImporter.load(modelStream, "model");
		        loadedModel = ColladaImporter.getModel();

				} else {

					ByteArrayOutputStream BO = new ByteArrayOutputStream();
					formatConverter.convert(originalFile.toURL().openStream(), BO);

					//loadedModel = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
					loadedModel = new Node("trimeshcoming");
					Spatial tmp = (Spatial) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
					loadedModel.attachChild(tmp);

				}

				BinaryExporter.getInstance().save(loadedModel, convertedFile);
				convertedFile.setLastModified(originalFile.lastModified());


			} catch (IOException e) {
				throw new ModelException("Could not convert the model " + convertedFile + ": " + e);
			}

		} else {

			try {
				// Import mesh (cast from Loadable to Node)

				BinaryImporter binaryImporter = new BinaryImporter();

				/*
				try {
		        MultiFormatResourceLocator loc2 = new MultiFormatResourceLocator(new URI("data/texture"), ".jpg", ".png", ".tga");
		        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, loc2);
		    } catch (URISyntaxException e) {
		    	throw new ModelException("Error setting resource locator for model textures.", e);
		    }
		    */

				loadedModel = (Node) binaryImporter.load(convertedFile);

			} catch (Exception e) {
				throw new ModelException("Error loading model " + convertedFile, e);
			}

		}

		return loadedModel;
	}

	/**
	 * Searches for a child with a given name
	 * @param spatial
	 * @param name
	 * @return
	 */
	public static Spatial findChild(Spatial spatial, String name) {

		if (name.equals(spatial.getName())) return spatial;

		if (spatial instanceof Node) {
			List<Spatial> children = ((Node)spatial).getChildren();
			if (children != null) {
				for (Spatial child : children) {
					Spatial result = ModelUtil.findChild (child, name);
					if (result != null) return result;
				}
			}
		}

		return null;
	}

	/*
	public static void updateRenderStateRecursive(Spatial spatial) {
		spatial.updateRenderState();
		if (spatial instanceof Node) {
			List<Spatial> children = ((Node)spatial).getChildren();
			if (children != null) {
				for (Spatial child : children) {
					updateRenderStateRecursive(child);
				}
			}
		}
	}
	*/

}
