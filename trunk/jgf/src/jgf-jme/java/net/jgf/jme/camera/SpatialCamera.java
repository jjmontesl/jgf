
package net.jgf.jme.camera;



import net.jgf.config.Config;
import net.jgf.config.Configurable;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

/**
 * A camera attached to a Viewable entity.
 */
@Configurable
public class SpatialCamera extends JmeCamera {

	protected Spatial spatial;

	public SpatialCamera() {
		super();
	}

	public SpatialCamera(Spatial spatial) {
		super();
		this.spatial = spatial;
	}

	public SpatialCamera(String id) {
		super(id);
	}

	public SpatialCamera(String id, Spatial spatial) {
		super(id);
		this.spatial = spatial;
	}

	/**
	 * Updates the camera using its controller
	 */
	@Override
	public void update(float tpf) {

		Camera camera = this.getCamera();
		camera.setLocation(spatial.getWorldTranslation());
		camera.lookAt(spatial.getWorldRotation().mult(Vector3f.UNIT_Z).addLocal(spatial.getWorldTranslation()),
				spatial.getWorldRotation().mult(Vector3f.UNIT_Y));

		DisplaySystem display = DisplaySystem.getDisplaySystem();
		// TODO: FOV, Far plane, Near plane, should be part of the camera config
		//camera.setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.1f, 800 );
		//camera.setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.01f, 1000 );
		camera.setFrustumPerspective( 80.0f, (float) display.getWidth() / (float) display.getHeight(), 0.1f, 800 );
		camera.update();
	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}



}
