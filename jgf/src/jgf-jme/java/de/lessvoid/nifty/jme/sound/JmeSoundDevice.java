package de.lessvoid.nifty.jme.sound;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import com.jme.util.resource.ClasspathResourceLocator;
import com.jme.util.resource.MultiFormatResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.AudioTrack.TrackType;

import de.lessvoid.nifty.jme.render.JmeRenderDevice;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * <code>JmeSoundDevice</code> is a SoundDevice implementation for JME2
 * @author larynx
 *
 */
public class JmeSoundDevice implements SoundDevice 
{
	// The jme2 audio system
	AudioSystem audio;
	
	// Add common logging and serial
	private static final Logger logger = Logger.getLogger(JmeRenderDevice.class.getName());
	private static final long serialVersionUID = 1L;
	
	public JmeSoundDevice()
	{
		// grab a handle to the jme2 audio system.
		audio = AudioSystem.getSystem();
		
		// setup the resource locator for audio files
		File file = new File(".").getAbsoluteFile();
		ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_AUDIO, new MultiFormatResourceLocator(file.toURI()));
		ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_AUDIO, new ClasspathResourceLocator());
	}
  
	@Override
	public SoundHandle loadMusic(SoundSystem soundSystem, String filename) 
	{	      
		// load the sound file into an AudioTrack ready for playing
		try
		{
			URL url = getSoundUrl(filename);
			if (url != null)
			{
				AudioTrack music = getMusic(url);
				return new JmeSoundHandle(music);
			}
			else
			{
				logger.warning("Music " + filename + " was not found");
				return null;
			}
		}
		catch (Exception e)
		{
			logger.severe("Music " + filename + ": " + e.getMessage());
		}
		return null;
	}
  
	@Override
	public SoundHandle loadSound(SoundSystem soundSystem, String filename) 
	{
		// load the sound file into an AudioTrack ready for playing
		try
		{
			URL url = getSoundUrl(filename);		
			if (url != null)
			{
				AudioTrack sfx = getSFX(url);		
				return new JmeSoundHandle(sfx);
			}
			else
			{
				logger.warning("Sound " + filename + " was not found");
				return null;
			}
		}
		catch (Exception e)
		{
			logger.severe("Sound " + filename + ": " + e.getMessage());
		}
		return null;
	}
  
	@Override
	public void update(int delta) 
	{
        // update the jme2 audio system
        audio.update();
	}  
  
	/**
	 * Creates an AudioTrack from an URL
	 * @param resource the URL of the sound file
	 * @return a new AudioTrack ready for playing
	 */
	private AudioTrack getMusic(URL resource) throws IOException
	{
		// Create a streaming, non-looping, relative sound clip.
		AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, true);
		
		if (sound != null)
		{
			sound.setType(TrackType.MUSIC);
			sound.setRelative(true);		
			sound.setLooping(false);
		}
		else
		{
			throw(new IOException("Failed to create audiotrack"));
		}
				
		return sound;
	}

	/**
	 * Creates an AudioTrack from an URL
	 * @param resource the URL of the sound file
	 * @return a new AudioTrack ready for playing
	 */
	private AudioTrack getSFX(URL resource) throws IOException
    {
        // Create a non-streaming, non-looping, non-positional sound clip.
        AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, false);
        
		if (sound != null)
		{
	        sound.setType(TrackType.HEADSPACE);
	        sound.setRelative(false);
	        sound.setLooping(false);
		}
		else
		{
			throw(new IOException("Failed to create audiotrack"));
		}

        return sound;
    }	
	
	/**
	 * <code>getSoundUrl</code> tries to find the absolute URL
	 * @param filename the relative path to a sound file
	 * @return
	 */
	private URL getSoundUrl(String filename)
	{
		// try the locator first
		URL url = null;
		if (!filename.startsWith("/"))
		{
			url = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_AUDIO, "/" + filename);
		}
		if (url == null)
		{
			url = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_AUDIO, filename);
		}
		// last chance - try the classpath
		if (url == null)
		{
			url = JmeSoundDevice.class.getResource(filename);
		}
		if (url == null)
		{
			if (!filename.startsWith("/"))
			{
				url = JmeSoundDevice.class.getResource("/" + filename);
			}
		}	
		return url;		
	}
}
