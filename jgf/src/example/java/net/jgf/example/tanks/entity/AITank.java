
package net.jgf.example.tanks.entity;

import net.jgf.config.Configurable;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.system.Jgf;

import com.jme.math.Vector3f;

/**
 */
@Configurable
public class AITank extends Tank {

	protected Vector3f targetPos = Vector3f.ZERO.clone();

	protected SpatialEntity targetEntity = null;



	/* (non-Javadoc)
	 * @see net.jgf.example.tanks.entity.Tank#load()
	 */
	@Override
	public void load() {
		super.load();
		Jgf.getDirectory().register(this, "targetEntity", "entity/root/players/player1");
	}

	@Override
	public void update(float tpf) {

		if (targetEntity != null) targetPos.set(targetEntity.getSpatial().getLocalTranslation());

		direction.set(targetPos.subtract(spatial.getLocalTranslation()));
		direction.normalizeLocal().multLocal(0.2f);

		super.update(tpf);

	}

	/**
	 * @return the targetPos
	 */
	public Vector3f getTargetPos() {
		return targetPos;
	}

	/**
	 * @param targetPos the targetPos to set
	 */
	public void setTargetPos(Vector3f targetPos) {
		this.targetPos = targetPos;
	}

	/**
	 * @return the targetEntity
	 */
	public SpatialEntity getTargetEntity() {
		return targetEntity;
	}

	/**
	 * @param targetEntity the targetEntity to set
	 */
	public void setTargetEntity(SpatialEntity targetEntity) {
		this.targetEntity = targetEntity;
	}



}
