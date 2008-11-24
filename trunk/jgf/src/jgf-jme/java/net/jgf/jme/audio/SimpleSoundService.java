
package net.jgf.jme.audio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**

 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public final class SimpleSoundService extends BaseService {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SimpleSoundService.class);

	protected Map<String, AudioItem> audioItems;




	public SimpleSoundService() {
		super();
		audioItems = new HashMap<String, AudioItem>();
	}

	/**
	 * Configures additional rules for the commons-digester library.
	 */
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);

		List<AudioItem> audioItemsTemp = ConfigurableFactory.newListFromConfig(config, configPath + "/audio", AudioItem.class);
		for (AudioItem audioItem : audioItemsTemp) {
			this.addAudioItem(audioItem);
			Jgf.getDirectory().addObject(audioItem.getId(), audioItem);
		}

	}

	public void addAudioItem(AudioItem item) {
		if (audioItems.containsKey(item.getId())) {
			throw new ConfigException("Tried to add AudioItem with an existing name '" + item.getId() + "' to " + this);
		}
		audioItems.put(item.getId(), item);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + id +", audioItems=" + audioItems.size() + "]";
	}



}
