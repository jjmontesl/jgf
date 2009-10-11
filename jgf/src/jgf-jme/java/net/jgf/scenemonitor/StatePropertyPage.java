
package net.jgf.scenemonitor;



import net.jgf.config.Configurable;
import net.jgf.core.state.State;

import org.apache.log4j.Logger;

import com.acarter.propertytable.Property;
import com.acarter.propertytable.PropertySection;
import com.acarter.propertytable.PropertySectionState;
import com.acarter.propertytable.PropertyTable;
import com.acarter.propertytable.propertyobject.BooleanPropertyObject;
import com.acarter.propertytable.propertyobject.BooleanPropertyObject.I_BooleanPropertyObjectListener;
import com.acarter.scenemonitor.propertydescriptor.A_PropertyPage;

/**
 * <p>This state manages the level rendering. If disabled, no level
 * rendering will happen.</p>
 */
@Configurable
public class StatePropertyPage extends A_PropertyPage {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(StatePropertyPage.class);

	protected State state;

	public StatePropertyPage() {
		super();
		
		// Define properties
		PropertySection stateSection = new PropertySection("State");
		stateSection.addProperty(new Property("Active", new BooleanPropertyObject()));
		stateSection.addProperty(new Property("Loaded", new BooleanPropertyObject()));
		stateSection.setState(PropertySectionState.EXPANDED);
		model.addPropertySection(0, stateSection);
		
	}

    @Override
    public void updateModel(PropertyTable table, Object object) {

            if(object instanceof State) {
                    
                    State state = (State)object;
                    
                    if(this.state == null || !this.state.equals(state)) {
                            
                            this.state = state;
                            updateListeners(state);
                    }
            }
    }

    protected void updateListeners(final State state) {
            
        BooleanPropertyObject active = (BooleanPropertyObject)model.getPropertySection("State").getProperty("Active").getPropertyObject();
        active.SetListener(new I_BooleanPropertyObjectListener() {
                public boolean readValue() {
                    return state.isActive();
                }
                public void saveValue(boolean value) {
                    if ((value == true) && (!state.isActive())) {   
                    	state.activate();
                    } else if ((value == false) && (state.isActive())) {
                    	state.deactivate();
                    }
                }
        });     
        
        BooleanPropertyObject loaded = (BooleanPropertyObject)model.getPropertySection("State").getProperty("Loaded").getPropertyObject();
        loaded.SetListener(new I_BooleanPropertyObjectListener() {
            public boolean readValue() {
                return state.isLoaded();
            }
            public void saveValue(boolean value) {
                if ((value == true) && (!state.isLoaded())) {   
                	state.load();
                } else if ((value == false) && (state.isLoaded())) {
                	state.unload();
                }
            }
    });     
    }
    

}
