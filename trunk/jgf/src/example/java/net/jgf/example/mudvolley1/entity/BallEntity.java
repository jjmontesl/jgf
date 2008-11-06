
package net.jgf.example.mudvolley1.entity;



import net.jgf.example.mudvolley1.MudSettings;
import net.jgf.jme.entity.SceneEntity;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 */
public class BallEntity extends SceneEntity {

	private Vector3f speed = new Vector3f(Vector3f.ZERO);

	private Vector3f randomAxis = new Vector3f();

	private Quaternion rotation = new Quaternion();

	@Override
	public void update(float tpf) {
		spatial.getLocalTranslation().addLocal(speed.mult(tpf));
		spatial.getLocalRotation().multLocal(rotation);
		speed.y += (MudSettings.BALL_GRAVITY * tpf);

	}

	public void bounce() {
		randomAxis.set(FastMath.nextRandomFloat(),FastMath.nextRandomFloat(), FastMath.nextRandomFloat()).normalizeLocal();
		rotation.fromAngleAxis(FastMath.nextRandomFloat() * MudSettings.BALL_ROTATION * (speed.x / MudSettings.BALL_BOUNCE),
				randomAxis);
	}

	/**
	 * @return the speed
	 */
	public Vector3f getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(Vector3f speed) {
		this.speed = speed;
	}



}
