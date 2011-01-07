
package net.jgf.view;



import net.jgf.core.state.BaseState;

import org.apache.log4j.Logger;

/**
 *
 */
public abstract class BaseViewState extends BaseState implements ViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseViewState.class);

	@Override
	public final void render(float tpf) {
	    if (this.isActive()) doRender(tpf);
	}

	@Override
	public final void update(float tpf) {
	    if (this.isActive()) doUpdate(tpf);
	}

	@Override
	public final void input(float tpf) {
	    if (this.isActive()) doInput(tpf);
	}

    public void doRender(float tpf) {
        
    }
   
    public void doUpdate(float tpf) {
        
    }
    
    public void doInput(float tpf) {
        
    }
	
}
