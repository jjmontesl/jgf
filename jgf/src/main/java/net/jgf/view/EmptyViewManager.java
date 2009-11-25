package net.jgf.view;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.service.BaseService;
import net.jgf.core.state.State;
import net.jgf.core.state.StateObserver;

/**
 * Temporary empty view manager. This view manager is used for the dedicated
 * server for testing. Currently it is not possible to load a configuration XML
 * without a view manager. When a more permenent solution is found it will be
 * removed.
 * @version 1.0
 */
@Configurable
public final class EmptyViewManager extends BaseService implements ViewManager {

    /**
     * Constructor.
     */
    public EmptyViewManager() {
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }

    @Override
    public State getRootState() {
        return new EmptyViewState();
    }

    @Override
    public void setRootState(ViewState rootState) {
    }

    @Override
    public void input(float tpf) {
    }

    @Override
    public void render(float tpf) {
    }

    @Override
    public void update(float tpf) {
    }
    
    public class EmptyViewState implements ViewState {

        @Override
        public void input(float tpf) {
        }

        @Override
        public void render(float tpf) {
        }

        @Override
        public void update(float tpf) {
        }

        @Override
        public void activate() {
        }

        @Override
        public void addStateObserver(StateObserver observer) {
        }

        @Override
        public void clearStateObservers() {
        }

        @Override
        public void deactivate() {
        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public boolean isAutoActivate() {
            return false;
        }

        @Override
        public boolean isAutoLoad() {
            return false;
        }

        @Override
        public boolean isLoaded() {
            return false;
        }

        @Override
        public void load() {
        }

        @Override
        public void removeStateObserver(StateObserver observer) {
        }

        @Override
        public void unload() {
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public void setId(String id) {
        }
        
    }

}
