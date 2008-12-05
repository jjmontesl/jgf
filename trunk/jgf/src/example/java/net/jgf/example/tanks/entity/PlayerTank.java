
package net.jgf.example.tanks.entity;

import net.jgf.config.Configurable;

import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

/**
 */
@Configurable
public class PlayerTank extends Tank {

	private boolean walkLeft;

	private boolean walkRight;

	private boolean walkUp;

	private boolean walkDown;

	protected final Vector3f target = new Vector3f();

	@Override
	public void update(float tpf) {

		direction.set(0, 0, 0);
		if (walkLeft) direction.x = -1;
		if (walkRight) direction.x = 1;
		if (walkUp) direction.z = -1;
		if (walkDown) direction.z = 1;
		direction.normalizeLocal();

		// Canon
		Spatial canon = ((Node)((Node)spatial).getChild("Tank")).getChild("Canon");
		target.setY(canon.getLocalTranslation().y);
		canon.lookAt(target, Vector3f.UNIT_Y);

		fixCursor();

		super.update(tpf);



	}

	protected void fixCursor() {
		// Don't let cursor get too away
		Vector3f distanceToTarget = target.subtract(spatial.getLocalTranslation());
		if (distanceToTarget.length() > 15) {
			distanceToTarget.normalizeLocal().multLocal(15).addLocal(spatial.getLocalTranslation());
			Vector3f newPos = DisplaySystem.getDisplaySystem().getRenderer().getCamera().getScreenCoordinates(distanceToTarget);
			cursorView.getMouse().setLocalTranslation(newPos);
			MouseInput.get().setCursorPosition((int)newPos.x, (int)newPos.y);
		}
	}

	/**
	 * @return the walkLeft
	 */
	public boolean isWalkLeft() {
		return walkLeft;
	}

	/**
	 * @param walkLeft the walkLeft to set
	 */
	public void setWalkLeft(boolean walkLeft) {
		this.walkLeft = walkLeft;
	}

	/**
	 * @return the walkRight
	 */
	public boolean isWalkRight() {
		return walkRight;
	}

	/**
	 * @param walkRight the walkRight to set
	 */
	public void setWalkRight(boolean walkRight) {
		this.walkRight = walkRight;
	}

	/**
	 * @return the walkUp
	 */
	public boolean isWalkUp() {
		return walkUp;
	}

	/**
	 * @param walkUp the walkUp to set
	 */
	public void setWalkUp(boolean walkUp) {
		this.walkUp = walkUp;
	}

	/**
	 * @return the walkDown
	 */
	public boolean isWalkDown() {
		return walkDown;
	}

	/**
	 * @param walkDown the walkDown to set
	 */
	public void setWalkDown(boolean walkDown) {
		this.walkDown = walkDown;
	}

	/**
	 * @return the target
	 */
	public Vector3f getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Vector3f target) {
		this.target.set(target);
	}

}