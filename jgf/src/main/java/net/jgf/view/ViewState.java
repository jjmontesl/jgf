
package net.jgf.view;

import net.jgf.core.state.State;





/**
 *
 */
public interface ViewState extends State {

	public void render(float tpf);

	public void update(float tpf);

	public void input(float tpf);

}
