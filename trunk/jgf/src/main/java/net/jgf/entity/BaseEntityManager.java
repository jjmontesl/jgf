package net.jgf.entity;

import net.jgf.config.Config;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.system.Jgf;

public abstract class BaseEntityManager extends BaseService implements EntityManager {

	/**
	 * Entities, by shortId
	 */
	protected Entity rootEntity;

	public BaseEntityManager() {
		super();
	}

	@Override
	public void update(float tpf) {
		rootEntity.update(tpf);
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.rootEntity = ConfigurableFactory.newFromConfig(config, configPath + "/entity", Entity.class);
		Jgf.getDirectory().addObject(rootEntity.getId(), rootEntity);

	}

	/**
	 * @return the rootEntity
	 */
	@Override
	public Entity getRootEntity() {
		return rootEntity;
	}

	/**
	 * @param rootEntity the rootEntity to set
	 */
	@Override
	public void setRootEntity(Entity rootEntity) {
		this.rootEntity = rootEntity;
	}

	@Override
	public void dispose() {
		super.dispose();
		this.rootEntity.unload();
	}

}