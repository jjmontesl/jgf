package de.lessvoid.nifty.jme.sound;

import com.jmex.audio.AudioTrack;

import de.lessvoid.nifty.spi.sound.SoundHandle;

/**
 * <code>JmeSoundHandle</code> is a SoundHandle implementation for JME2
 * @author larynx
 *
 */
public class JmeSoundHandle implements SoundHandle
{
	/**
	 * the jme2 audio track
	 */
	AudioTrack track;
	
	public JmeSoundHandle(AudioTrack track)
	{
		this.track = track;
	}

	@Override
	public float getVolume() 
	{		
		return track.getVolume();
	}
	@Override
	public void setVolume(float volume) 
	{
		track.setVolume(volume);
	}

	@Override
	public boolean isPlaying() 
	{
		return track.isPlaying();
	}
	@Override
	public void play() 
	{
		track.play();		
	}
	@Override
	public void stop() 
	{
		track.stop();
	}
	
	@Override
	public void dispose() 
	{
		track.release();
	}  
}
