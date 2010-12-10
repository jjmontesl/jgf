
package net.jgf.jme.loader.model;

import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.jme.model.ModelException;
import net.jgf.jme.model.util.ModelUtil;
import net.jgf.loader.LoadProperties;

import com.jme.scene.Node;



/**
 *
 */
// TODO: Organize conversion and loading strategy
@Configurable
public class ConverterLoader extends ModelLoader {

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

		return node;

	}


}
