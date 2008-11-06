
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.entity.EntityManager;
import net.jgf.logic.BaseLogicState;
import net.jgf.scene.SceneManager;
import net.jgf.system.System;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class InGameLogic extends BaseLogicState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(InGameLogic.class);

	private SceneManager sceneManager;

	private EntityManager entityManager;

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {
		super.load();
		entityManager = System.getDirectory().getObjectAs("entity", EntityManager.class);
		sceneManager = System.getDirectory().getObjectAs("scene/manager", SceneManager.class);
	}



	@Override
	public void update(float tpf) {

		sceneManager.update(tpf);
		entityManager.update(tpf);

	}


}
