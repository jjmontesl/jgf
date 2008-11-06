
package net.jgf.example.tanks.loader;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.camera.StaticCamera;
import net.jgf.jme.refs.SpatialReference;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.scene.SceneCreatorLoader;
import net.jgf.scene.Scene;

import org.apache.log4j.Logger;

import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.resource.ResourceLocatorTool;

/**
 */
@Configurable
public final class TanksSceneLoader extends SceneCreatorLoader {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(TanksSceneLoader.class);

	String[] dataArray;

	int width;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		width = config.getInt(configPath + "/width");
		String rawData = config.getString(configPath + "/map");

		dataArray = rawData.split("\\s+");

	}

	/**
	 * Loads a scene, including scene data
	 */
	@Override
	public Scene load(Scene base, LoadProperties properties) {

		DefaultJmeScene scene = (DefaultJmeScene) base;

		// Prepare some resources
    Texture floorTexture = TextureManager.loadTexture(
    	ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "mudvolley/texture/hardwoodfloor.jpg"),
			Texture.MinificationFilter.NearestNeighborLinearMipMap,
			Texture.MagnificationFilter.NearestNeighbor);
  	TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    ts.setTexture(floorTexture, 0);
    ts.setEnabled(true);

    Node fieldNode = new Node("fieldNode");
    Node floorNode = new Node("floorNode");
    Node obstaclesNode = new Node("obstaclesNode");

		// Generate map
		int row = 0;
		int col = 0;
		int playerStartCount = 0;
		for (int i = 0; i < dataArray.length; i++) {

			int height = 0;
			String data = dataArray[i];

			// Walls
			if ((data.charAt(0) >= '0') && (data.charAt(0) <= '9')) {
				height = Integer.parseInt(data);
			}

			// Holes
			if (data.charAt(0) == '-') height = -1;

			// Player
			if (data.charAt(0) == 'P') {
				Node playerStartNode = new Node("playerStart" + playerStartCount + "Node");
				playerStartNode.setLocalTranslation(new Vector3f(0.5f + col, 0.0f, 0.5f + row));
				SpatialReference playerStart = new SpatialReference("playerStart" + playerStartCount, playerStartNode);
				scene.getReferences().addReference(playerStart);
				playerStartCount++;
			}

			if (height > -1.5f) {
		    Box floor = new Box("cell_" + row + "_" + col,
		    		new Vector3f(col, -2, row), new Vector3f(col + 1, 0.5f * height, row + 1));
		    floor.clearRenderState(RenderState.RS_TEXTURE);
		    //quad.setLightCombineMode(Spatial.LightCombineMode.Off);
		    floor.setTextureCombineMode(TextureCombineMode.Replace);
		    floor.setCullHint(CullHint.Never);
		    floor.setModelBound(new OrientedBoundingBox());
		    floor.updateModelBound();
		    floor.setRenderState(ts);

		    if (height > 0.1f) {
		    	obstaclesNode.attachChild(floor);
		    } else {
		    	floorNode.attachChild(floor);
		    }
			}

	    col++;
			if (col >= width) { col = 0; row++; }
		}

		scene.getCameraControllers().addCameraController(new StaticCamera(
				"scene/camera/test", new Vector3f(0.5f * width, 0.9f * width, 0.7f * row), new Vector3f(0.5f * width, 0, 0.5f * row)
		));
		net.jgf.system.System.getDirectory().addObject("scene/camera/test", scene.getCameraControllers().getCameraController("scene/camera/test"));

		fieldNode.attachChild(floorNode);
		fieldNode.attachChild(obstaclesNode);

    fieldNode.getLocalTranslation().addLocal(0, 0, 0);
    // TODO: Study how collisisions and rendering are affected by the hierarchy of boundings
    fieldNode.updateGeometricState(0, true);
    fieldNode.lock();

    scene.getRootNode().attachChild(fieldNode);

    // Updates
    scene.getRootNode().updateRenderState();
    scene.getRootNode().updateGeometricState(0, true);

		// Restart timer
		Timer.getTimer().reset();

		return scene;

	}

}
