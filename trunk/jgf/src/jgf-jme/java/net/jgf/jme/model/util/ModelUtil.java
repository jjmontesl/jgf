
package net.jgf.jme.model.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

public class ModelUtil {

	private static final Logger logger = Logger.getLogger(ModelUtil.class);

	private static URL getParentURL(URL url) throws MalformedURLException {
	    String urlString = url.toExternalForm();
	    urlString = urlString.replaceAll("\\%2[fF]", "/");
	    int lastSlash = urlString.lastIndexOf('/');
	    if (lastSlash > 0) {
	        urlString = urlString.substring(0, lastSlash + 1);
	    }
	    URL result = null;
	    result = new URL(urlString);
	    return result;
	}
	
	/**
	 *  This method opens a model in various format evaluating the extension.
	 */
	public static Node importModel (String resourceName) throws ModelException {

		logger.debug("Importing model from resource '" + resourceName + "'");

		Node loadedModel = null;
		FormatConverter formatConverter = null;

		String originalExtension = resourceName.substring(resourceName.lastIndexOf(".") + 1, resourceName.length());

		URL originalURL = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_MODEL, resourceName);
		if (originalURL == null) throw new ConfigException("Couldn't find resource " + resourceName + " when loading a model");

        // Resource locator
		URI parentUri = null;
		try {
		    logger.debug ("Importing model [originalUrl=" + originalURL.toExternalForm() + ",parentUrl=" + getParentURL(originalURL).toExternalForm() + "]");
		    parentUri = getParentURL(originalURL).toURI();
		} catch (URISyntaxException e) {
		    throw new ModelException("Could not find parent URL of " + originalURL.toExternalForm(), e);
		} catch (MalformedURLException e) {
            throw new ModelException("Could not find parent URL of " + originalURL.toExternalForm(), e);
        }
		
    	
		SimpleResourceLocator locator = new SimpleResourceLocator(parentUri);
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);

        
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
			URL matlib = null;
			try {
		        matlib = new URL(getParentURL(originalURL), "fir.mtl");
                formatConverter.setProperty("mtllib", matlib);
            } catch (MalformedURLException e) {
                throw new ModelException ("Could not set material library for obj to parent URL of '" + originalURL.toExternalForm() + "'", e);
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
				formatConverter.convert(originalURL.openStream(), BO);

				//loadedModel = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
				loadedModel = new Node("trimeshcoming");
				Spatial tmp = (Spatial) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
				loadedModel.attachChild(tmp);

			}


		} catch (IOException e) {
			throw new ModelException("Could not convert model " + resourceName, e);
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
