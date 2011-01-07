
package net.jgf.entity;

import net.jgf.core.state.BaseState;


/**
 * Each one of the game entities which are not part of the static map.
 * An Entity is server aware, meaning that if it is running on a dedicated server,
 * will avoid to do certain work.
 */
public abstract class BaseEntity extends BaseState implements Entity {

	public BaseEntity() {
		this(null);
	}



	public BaseEntity(String id) {
		super(id);
		this.autoActivate = true;
		this.autoLoad = true;
	}



	@Override
	public final void update(float tpf) {
	    if (this.isActive()) doUpdate(tpf);
	}

	public void doUpdate(float tpf) {
	    
	}
	
}
