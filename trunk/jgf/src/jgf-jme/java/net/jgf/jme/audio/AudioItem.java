
package net.jgf.jme.audio;

import java.net.URL;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.IllegalStateException;
import net.jgf.core.component.BaseComponent;

import org.apache.log4j.Logger;

import com.jme.util.resource.ResourceLocatorTool;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.AudioTrack.TrackType;
import com.jmex.audio.MusicTrackQueue.RepeatType;


/**

 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class AudioItem extends BaseComponent {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(AudioItem.class);

	protected String resourceUrl;

	protected AudioTrack audioTrack;

	/**
	 * Configures additional rules for the commons-digester library.
	 */
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);
		this.setResourceUrl(config.getString(configPath + "/resourceUrl"));
	}

	public void play() {
		lazyInitialize();
		MusicTrackQueue queue = AudioSystem.getSystem().getMusicQueue();
		queue.setCrossfadeinTime(0);
		queue.setRepeatType(RepeatType.NONE);
		queue.addTrack(audioTrack);
		queue.play();
	}

	protected void lazyInitialize() {
		if (audioTrack == null) {
			URL url = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_AUDIO, resourceUrl);
			audioTrack = AudioSystem.getSystem().createAudioTrack(url, false);
			if (audioTrack == null) {
				logger.warn("Could not load audio from " + resourceUrl);
			}
			audioTrack.setType(TrackType.HEADSPACE);
		}
	}

	protected String getResourceUrl() {
		return resourceUrl;
	}

	protected void setResourceUrl(String resourceUrl) {
		if (this.resourceUrl != null) {
			throw new IllegalStateException("Tried to assign a resourceUrl to an AudioItem, but AudioItem can only be initialized once");
		}
		this.resourceUrl = resourceUrl;
	}



}
