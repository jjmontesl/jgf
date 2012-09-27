package net.jgf.example.tanks.entity;

import java.util.ArrayList;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.example.tanks.view.EffectsView;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.jme.model.util.ModelUtil;
import net.jgf.jme.model.util.TransientSavable;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.intersection.BoundingCollisionResults;
import com.jme.intersection.CollisionData;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.PickResults;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.BlendState.BlendEquation;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.scene.state.MaterialState.ColorMaterial;
import com.jme.system.DisplaySystem;

/**
 */
@Configurable
public class Explosion extends SpatialEntity {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Explosion.class);

    @Register (ref = "scene")
    private DefaultJmeScene scene;
    
    @Register (ref = "entity/root/explosions")
    private EntityGroup explosionsGroup;
    
    @Register (ref = "loader/entity/pool")
    private EntityPoolLoader entityLoader;

    public Quaternion rotation = null;
    
    public ColorRGBA fadecolor = null;
    
    public float ttl;
    
    public float initialTtl;
    
	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#load()
	 */
	@Override
	public void doLoad() {
		super.doLoad();
		
		BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(SourceFunction.SourceAlpha);
        bs.setDestinationFunction(DestinationFunction.One);
        bs.setBlendEquation(BlendEquation.Add);
        bs.setEnabled(true);

        TriMesh mesh = (TriMesh) ModelUtil.findChild(this.getSpatial(),
                "Sphere_001-explosion_jpg");
        fadecolor = new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
        mesh.setDefaultColor(fadecolor);

        MaterialState ms = (MaterialState) mesh.getRenderState(RenderState.RS_MATERIAL);
        ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
        // ms.getSpecular().set(0,0,0,0);
        // ms.setEnabled(false);

        mesh.setRenderState(bs);
        this.getSpatial().setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
        mesh.updateRenderState();

        mesh.setRenderState(((DefaultJmeScene) scene).getCommonRenderStates()
                .get("zBufferReadOnly"));

	}



	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#unload()
	 */
	@Override
	public void doUnload() {
		super.doUnload();
	}

	
	@Override
	public void doActivate() {
		super.doActivate();
	}


	@Override
	public void doUpdate(float tpf) {


	    this.ttl -= tpf;
        if (this.ttl > 0) {
            float scaleTpf = tpf * ((this.ttl) * (this.ttl) * 1.0f);
            this.spatial.setLightCombineMode(LightCombineMode.Off);
            this.spatial.getLocalScale().addLocal(scaleTpf, scaleTpf, scaleTpf);

            this.spatial.getLocalRotation().multLocal(this.rotation);

            this.fadecolor.a = (this.ttl) / this.initialTtl;

            this.spatial.updateRenderState();

        } else {
            Node explosionNode = (Node) scene.getRootNode();
            this.withdraw(explosionsGroup, explosionNode);

            StateHelper.deactivate(this);
            entityLoader.returnToPool(this);
        }

	}
	
}
