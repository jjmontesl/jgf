package net.jgf.example.mudvolley1.entity;



import net.jgf.config.Configurable;
import net.jgf.entity.Entity;
import net.jgf.example.mudvolley1.MudSettings;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.entity.EntityLoader;

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

			// Ball
	    Sphere sphere = new Sphere(entityId + "Sphere", 16, 16, MudSettings.PLAYER_RADIUS);
	    sphere.setRandomColors();
	    MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
	    ms.setEnabled(true);
	    ms.setAmbient(new ColorRGBA(1, 0, 0, 0));
	    sphere.setRenderState(ms);

	    Node node = new Node(entityId + "Node");
	    node.attachChild(sphere);
	    node.getLocalTranslation().addLocal(-7, -5, 0);
	    node.setModelBound( new BoundingBox() );
	    node.updateWorldVectors();
	    node.updateModelBound();
	    node.updateGeometricState(0, true);

	    PlayerEntity playerEntity = new PlayerEntity();
	    playerEntity.setSide(side);
	    playerEntity.setId(entityId);
	    playerEntity.setSpatial(node);

	    return playerEntity;
	}

	@Override
	public Entity load(Entity base, LoadProperties properties) {
		throw new UnsupportedOperationException("The generic load method is not available in " + this);
	}

}
