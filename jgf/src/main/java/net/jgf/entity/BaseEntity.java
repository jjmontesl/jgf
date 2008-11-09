
package net.jgf.entity;

import net.jgf.core.state.BaseState;


/**
 * Each one of the game entities which are not part of the static map.
 * An Entity is server aware, meaning that if it is running on a dedicated server,
 * will avoid to do certain work.
 */
public abstract class BaseEntity extends BaseState implements Entity {

	public BaseEntity() {
		super();
		this.autoActivate = true;
		this.autoLoad = true;
	}

	@Override
	public void update(float tpf) {

	}

}
