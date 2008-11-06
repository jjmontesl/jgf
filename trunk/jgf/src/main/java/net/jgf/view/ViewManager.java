package net.jgf.view;

import net.jgf.core.state.State;

public interface ViewManager {


	public void update(float tpf);

	public void input(float tpf);

	/**
	 * @param tpf
	 * @see net.jgf.view.ViewStateNode#render(float)
	 */
	public void render(float tpf);

	/**
	 * @return the rootState
	 */
	public State getRootState();

	/**
	 * @param rootState the rootState to set
	 */
	public void setRootState(ViewState rootState);

}