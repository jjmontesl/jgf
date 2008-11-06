package net.jgf.entity;


public interface EntityManager {

	/**
	 * Update entities 
	 */
	public void update(float tpf);

	/**
	 * @return the rootEntity
	 */
	public Entity getRootEntity();

	/**
	 * @param rootEntity the rootEntity to set
	 */
	public void setRootEntity(Entity rootEntity);

}