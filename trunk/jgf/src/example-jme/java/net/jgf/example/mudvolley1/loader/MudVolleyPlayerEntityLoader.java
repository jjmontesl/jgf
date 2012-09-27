package net.jgf.example.mudvolley1.loader;

import net.jgf.config.Configurable;
import net.jgf.entity.Entity;
import net.jgf.example.mudvolley1.MudSettings;
import net.jgf.example.mudvolley1.entity.PlayerEntity;
import net.jgf.jme.util.TypeParserHelper;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.entity.EntityLoader;

import org.apache.log4j.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

/**
 */
@Configurable
public final class MudVolleyPlayerEntityLoader extends EntityLoader {

    /**
     * Class logger
     */
    private static final Logger logger = Logger.getLogger(MudVolleyPlayerEntityLoader.class);

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
        node.setModelBound(new BoundingBox());
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
        this.checkNullBase(base);
        this.combineProperties(properties);
        String playerId = properties.get("MudVolleyPlayerEntityLoader.entityId");
        float side = TypeParserHelper.valueOfFloat(properties
                .get("MudVolleyPlayerEntityLoader.side"));
        return loadPlayer(playerId, side);
    }

}
