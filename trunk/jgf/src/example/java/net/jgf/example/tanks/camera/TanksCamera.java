
package net.jgf.example.tanks.camera;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.jme.camera.JmeCamera;
import net.jgf.jme.entity.SpatialEntity;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 */
@Configurable
public class TanksCamera extends JmeCamera {


	protected Vector3f location = new Vector3f();
	
	protected Vector3f lookAt = new Vector3f();

	@Register (ref="entity/root/players/player1")
	protected SpatialEntity target;

	protected Vector3f lastTarget = new Vector3f();
	
	public TanksCamera() {
        super();
    }

    public TanksCamera(String id) {
        super(id);
    }


    /**
	 * Updates the camera using its controller
	 */
	@Override
	public void update(float tpf) {

		if (target != null) {
			lastTarget.set(target.getSpatial().getWorldTranslation());
		}
			
		Vector3f lookAtPos = lastTarget.clone();
		lookAtPos.y = 0.5f;
		lookAtPos.z = lookAtPos.z - 0.5f;
		lookAt.interpolate(lookAtPos, 1.2f * tpf);

		Vector3f targetPos = lastTarget.clone();
		targetPos.y = 22.0f + 5.0f * (FastMath.sqrt(lookAtPos.distance(lookAt)));
		targetPos.z = targetPos.z + 3.5f;
		location.interpolate(targetPos, 0.7f * tpf);


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
