
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.entity.EntityManager;
import net.jgf.logic.BaseLogicState;
import net.jgf.scene.SceneManager;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class InGameLogic extends BaseLogicState {

	@Register (ref = "scene/manager")
	private SceneManager sceneManager;

	@Register (ref = "entity")
	private EntityManager entityManager;
	
	@Override
	public void doUpdate(float tpf) {

		entityManager.update(tpf);
		sceneManager.update(tpf);
		
	}

}
