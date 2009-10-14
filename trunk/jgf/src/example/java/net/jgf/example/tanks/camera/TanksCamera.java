
package net.jgf.example.tanks.camera;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.example.tanks.entity.Tank;
import net.jgf.jme.camera.JmeCamera;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.system.Jgf;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 */
@Configurable
public class TanksCamera extends JmeCamera {


	protected Vector3f location = new Vector3f();
	
	protected Vector3f lookAt = new Vector3f();

	protected SpatialEntity target;

	
	
	public TanksCamera() {
		super();
		Jgf.getDirectory().register(this, "target", "entity/root/players/player1");
	}


	public TanksCamera(String id) {
		super(id);
		Jgf.getDirectory().register(this, "target", "entity/root/players/player1");
	}


	
	public SpatialEntity getTarget() {
		return target;
	}


	public void setTarget(SpatialEntity target) {
		this.target = target;
	}


	/**
	 * Updates the camera using its controller
	 */
	@Override
	public void update(float tpf) {

		if (target != null) {
			
			Vector3f lookAtPos = target.getSpatial().getWorldTranslation().clone();
			lookAtPos.y = 0.5f;
			lookAtPos.z = lookAtPos.z - 0.5f;
			lookAt.interpolate(lookAtPos, 1.2f * tpf);

			Vector3f targetPos = target.getSpatial().getWorldTranslation().clone();
			targetPos.y = 22.0f + 5.0f * (FastMath.sqrt(lookAtPos.distance(lookAt)));
			targetPos.z = targetPos.z + 3.5f;
			location.interpolate(targetPos, 0.7f * tpf);
			
		}

		getCamera().setLocation(location);
		getCamera().lookAt(lookAt, Vector3f.UNIT_Y);
		getCamera().update();

	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}


}
