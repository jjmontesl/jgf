
package net.jgf.jme.loader.model;

import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.jme.model.ModelException;
import net.jgf.jme.model.util.ModelUtil;
import net.jgf.loader.LoadProperties;

import com.jme.scene.Node;
import com.jme.util.export.binary.BinaryImporter;



/**
 *
 */
// TODO: Cache in native jbin format,chaining with a subloader
@Configurable
public class CacheLoader extends ModelLoader {

	@Override
	public Node load(Node node, LoadProperties properties) throws ModelException {

		combineProperties(properties);

		String resourceUrl = properties.get("ConverterLoader.resourceUrl");
		if (resourceUrl == null) {
			throw new ConfigException("Loader " + this + " needs to receive a valid 'ConverterLoader.resourceUrl' property");
		}

		if (node == null) node = new Node(resourceUrl + "Node");
		Node subNode = ModelUtil.importModel(resourceUrl);
		node.attachChild(subNode);


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
/*
            loadedModel = (Node) binaryImporter.load(convertedFile);

        } catch (Exception e) {
            throw new ModelException("Error loading model " + convertedFile, e);
        }
        
        BinaryExporter.getInstance().save(loadedModel, convertedFile);
        convertedFile.setLastModified(originalFile.lastModified());
	*/	
		return null;

	}


}
