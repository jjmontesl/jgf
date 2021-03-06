
package net.jgf.example.tanks.entity;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;

/**
 */
@Configurable
public class PlayerTank extends Tank {

    @Register (ref="entity/root/links/self")
    private net.jgf.example.tanks.entity.Player self;
    
	private boolean walkLeft;

	private boolean walkRight;

	private boolean walkUp;

	private boolean walkDown;
	
	private int kills = 0;
	
	@Override
	public void doUpdate(float tpf) {

	    if ((this.player != null) && (this.player.getId().equals(self.getId()))) {
    		direction.set(0, 0, 0);
    		if (walkLeft) direction.x = -1;
    		if (walkRight) direction.x = 1;
    		if (walkUp) direction.z = -1;
    		if (walkDown) direction.z = 1;
    		direction.normalizeLocal();
	    }

		super.doUpdate(tpf);

		//fixCursor();

	}

	
	
	public int getKills() {
		return kills;
	}



	public void setKills(int kills) {
		this.kills = kills;
	}



	protected void fixCursor() {
		// Don't let cursor get too away
		/*
	    Vector3f distanceToTarget = target.subtract(spatial.getLocalTranslation());
		distanceToTarget.setY(0.5f);
		if (distanceToTarget.length() > 15) {
			distanceToTarget.normalizeLocal().multLocal(15).addLocal(spatial.getLocalTranslation());
			Vector3f newPos = DisplaySystem.getDisplaySystem().getRenderer().getCamera().getScreenCoordinates(distanceToTarget);
			//cursorView.getMouse().setLocalTranslation(newPos);
			MouseInput.get().setCursorPosition((int)newPos.x, (int)newPos.y);
		}
		*/
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

}
