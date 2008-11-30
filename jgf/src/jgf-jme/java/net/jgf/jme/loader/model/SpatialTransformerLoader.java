
package net.jgf.jme.loader.model;

import net.jgf.config.Configurable;
import net.jgf.jme.model.ModelException;
import net.jgf.jme.util.TypeParserHelper;
import net.jgf.loader.LoadProperties;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;



/**
 *
 */
// TODO: Organize conversion and loading strategy
@Configurable
public class SpatialTransformerLoader extends ModelLoader {

	@Override
	public Node load(Node node, LoadProperties properties) throws ModelException {

		if (node == null)  {
			throw new ModelException("SpatialTransformerLoader received a null Node to transform");
		}

		combineProperties(properties);

		String rotatePiString = properties.get("SpatialTransformerLoader.rotatePi");
		if (rotatePiString != null) {
			Vector3f rotatePi = TypeParserHelper.valueOfVector3f(rotatePiString);
			// TODO: We are replacing current rotation: bad!
			node.getLocalRotation().fromAngles(FastMath.PI * rotatePi.x, FastMath.PI * rotatePi.y, FastMath.PI * rotatePi.z);
		}

		Node result = new Node(node.getName() + "-trans");
		result.attachChild(node);

		return result;

	}



}
