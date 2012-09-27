
package net.jgf.example.mudvolley3.entity;



import net.jgf.config.Configurable;
import net.jgf.entity.Entity;
import net.jgf.example.mudvolley1.MudSettings;
import net.jgf.example.mudvolley1.entity.BallEntity;
import net.jgf.example.mudvolley1.entity.PlayerEntity;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.Loader;
import net.jgf.loader.entity.EntityLoader;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

/**
 */
@Configurable
public final class MudVolleyEntityLoader extends EntityLoader {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(MudVolleyEntityLoader.class);


	public BallEntity loadBall(String entityId) {

    // Ball
    Sphere ballSphere = new Sphere("ballSphere", 16, 16, MudSettings.BALL_RADIUS);
    ballSphere.setRandomColors();
    Texture ballTexture = TextureManager.loadTexture(
      	ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "mudvolley/texture/aquarela.jpg"),
  			Texture.MinificationFilter.NearestNeighborLinearMipMap,
  			Texture.MagnificationFilter.NearestNeighbor);
  	TextureState tsb = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
  	tsb.setTexture(ballTexture, 0);
  	tsb.setEnabled(true);
    ballSphere.setRenderState(tsb);
    MaterialState msb = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
    msb.setEnabled(true);
    msb.setAmbient(new ColorRGBA(0.8f, 0.8f, 0.8f, 0));
    ballSphere.setRenderState(msb);

    Node ballNode = new Node("ballNode");
    ballNode.attachChild(ballSphere);
    ballNode.getLocalTranslation().addLocal(0, 4, 0);
    ballNode.setModelBound( new BoundingBox() );
    ballNode.updateWorldVectors();
    ballNode.updateModelBound();
    //ballNode.updateGeometricState(0, true);

    BallEntity ballEntity = new BallEntity();
    ballEntity.setId(entityId);
    ballEntity.setSpatial(ballNode);

    return ballEntity;

	}

	public PlayerEntity loadPlayer(String entityId, float side) {

		Loader<Node> modelLoader = Jgf.getDirectory().getObjectAs("loader/model/mudvolley", Loader.class);

		LoadProperties loadProperties = new LoadProperties();
		loadProperties.put("ConverterLoader.resourceUrl", "mudvolley/model/playerRed.dae");
		loadProperties.put("SpatialTransformerLoader.rotatePi", side < 0 ? "-0.5 0.5 0.0" : "-0.5 -0.5 0.0");
		Node playerNode = modelLoader.load(new Node(entityId + "Node"), loadProperties);

		playerNode.setModelBound( new BoundingBox() );
		playerNode.updateWorldVectors();
		playerNode.updateModelBound();
		//playerNode.updateGeometricState(0, true);

		PlayerEntity playerEntity = new PlayerEntity();
	  playerEntity.setSide(side);
	  playerEntity.setId(entityId);
	  playerEntity.setSpatial(playerNode);

	  return playerEntity;

	}

	@Override
	public Entity load(Entity base, LoadProperties properties) {
		throw new UnsupportedOperationException("Generic load not suported by " + this);
	}

}
