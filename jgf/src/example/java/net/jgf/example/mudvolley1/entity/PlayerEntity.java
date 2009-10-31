
package net.jgf.example.mudvolley1.entity;

import net.jgf.example.mudvolley1.MudSettings;
import net.jgf.jme.entity.SpatialEntity;

import com.jme.math.FastMath;

/**
 */
public class PlayerEntity extends SpatialEntity {

	private float walkSpeed = MudSettings.PLAYER_WALK_SPEED;

	private boolean walkLeft;

	private boolean walkRight;

	private boolean jump;

	private float jumping;

	private float side;




	@Override
	public void update(float tpf) {

		if (walkLeft) spatial.getLocalTranslation().addLocal(-walkSpeed * tpf, 0, 0);
		if (walkRight) spatial.getLocalTranslation().addLocal(walkSpeed * tpf, 0, 0);

		jumping += (MudSettings.PLAYER_GRAVITY * tpf);
		spatial.getLocalTranslation().y += (jumping * tpf);

		if (spatial.getLocalTranslation().y < 0) {
			//jumping = 0;
			if (jump) {
				jumping = MudSettings.PLAYER_JUMP_SPEED;
				jump = false;
			}
			spatial.getLocalTranslation().y = 0;
		}

		// Limit player location to its game area
		if (FastMath.abs(spatial.getLocalTranslation().x) > (MudSettings.FIELD_WIDTH - MudSettings.PLAYER_RADIUS)) {
			spatial.getLocalTranslation().x = ((MudSettings.FIELD_WIDTH - MudSettings.PLAYER_RADIUS) * side);
		}
		if (FastMath.abs(spatial.getLocalTranslation().x) < (MudSettings.PLAYER_RADIUS + MudSettings.FIELD_NET_HALFWIDTH)) {
			spatial.getLocalTranslation().x = ((MudSettings.PLAYER_RADIUS + MudSettings.FIELD_NET_HALFWIDTH) * side);
		}

		spatial.updateWorldVectors();
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
	 * @return the jump
	 */
	public boolean isJump() {
		return jump;
	}

	/**
	 * @param jump the jump to set
	 */
	public void setJump(boolean jump) {
		this.jump = jump;
	}

	/**
	 * @return the side
	 */
	public float getSide() {
		return side;
	}

	/**
	 * @param side the side to set
	 */
	public void setSide(float side) {
		this.side = side;
	}



}
