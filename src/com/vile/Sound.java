package com.vile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import com.vile.launcher.FPSLauncher;

/**
 * @title Sound
 * @author Alexander Byrd
 * @date 5/10/2017
 * 
 * Description:
 * Keeps track of a a certain sound in the game, including all of its
 * clips, its volume control (FloatControl), the clip currently being
 * played (clipNum), and the audio stream being played from the audio
 * file (input). Can also play the sound, and update the values
 * accordingly.
 */
public class Sound 
{
	private Clip[] soundClip;
	private FloatControl[] soundControl;
	private int clipNum = 0;
	private AudioInputStream input;
	private int clipSize = 0;
	
	public Sound(int clipSize, String file) 
	{
		this.clipSize = clipSize;
		soundClip = new Clip[clipSize];
		soundControl = new FloatControl[soundClip.length];
		
		try
		{		
			for(int i = 0; i < clipSize; i++)
			{
				input = AudioSystem
						.getAudioInputStream(this
								.getClass()
								.getResource(
										file));
				
				soundClip[i] = AudioSystem.getClip();
	
				soundClip[i].open(input);
				
				soundControl[i] = (FloatControl) 
						soundClip[i].getControl
						(FloatControl.Type.MASTER_GAIN);
				
				soundClip[i].setMicrosecondPosition(0);
			}
			
			resetVolume(FPSLauncher.soundVolumeLevel);
		}
		catch (Exception e)
		{
			
		}
	}
	
   /**
    * Plays this audio clip, and makes sure to reset any audio clips of the
    * same type that are finished. If no audio clips are open, then resets
    * them all so that the process can restart again. shouldn't happen
    * unless all the clips are played, and the first clip is to be played 
    * on the next loop around.
    */
	public void playAudioFile(double distance)
	{
		//Tends to make sounds more realistic
		distance *= 2;
		
		//The clip will have a new volume depending on
		//the sources distance from the player
		double volume = ((100 - distance) / 100) 
				* FPSLauncher.soundVolumeLevel;
		
		//If 0 or below, just don't play the sound. Makes things faster
		if(volume <= 0)
		{
			return;
		}
		
	   /*
		* Basically, try to open the audio file, and throw an
		* exception if you can't. If the audio file is opened,
		* then set all 20 sound clips to that audio stream.
		* Once they are set (meaning they have been opened)
		* then play them one by one each time the sound is
		* played. After a clip finishes, its sound is restarted
		* at the begininning and is ready to play again, and
		* its audio Stream is closed to eliminate too many
		* threads opening causing the game to slow and
		* eventually crash.
		*/
		try 
		{
			boolean noActive = true;
			
			//If only a clip length of 1, then just reset that one clip
			//each time anyway.
			if(soundClip[clipNum].isActive())
			{
				stopClip();
			}
			
		   /*
		    * Checks beforehand for clips no longer active so the sound
		    * can always play. If no sound clips are open, open them all.
		    */
			for (int i = 0; i < soundClip.length; i++) 
			{
				if(!soundClip[i].isActive())
				{ 
					noActive = false;
					soundClip[i].setMicrosecondPosition(0);
				}
			}
			
			if(noActive)
			{
				//If any clip is no longer active, reset it.
				for (int i = 0; i < soundClip.length; i++) 
				{
					soundClip[i].setMicrosecondPosition(0);
				}
			}
			
			//Change the clips volume the amount calculated
			changeClipVolume((float)volume);
			
			//Close sound input thread
			input.close();
			
			//Start sound
			soundClip[clipNum].start();
			
			//Add to clips iterated through
			clipNum++;
			
			//If looped through all clips, reset the clip it is currently
			//on
			if(clipNum >= soundClip.length)
			{
				clipNum = 0;
			}
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}
	
   /**
    * Resets all clips of this sound to a new volume
    * @param newVolume
    */
	public void resetVolume(float newVolume)
	{
		float temp = 40f * (float) Math.log10
				(FPSLauncher.soundVolumeLevel / 100);
		
		if(temp < -80)
		{
			temp = -80;
		}
		
		//Set volume of all clips to a new volume
		for(int i = 0; i < clipSize; i++)
		{		
			soundControl[i].setValue
			(temp);
		}
	}
	
   /**
    * Sets only the current clip to a new volume.
    * Especially used when something is temporarily a new volume, such as
    * if a fireball hits far away and is lower volume. 
    * @param newVolume
    */
	public void changeClipVolume(float volume)
	{
		float temp = 40f * (float) Math.log10
				(volume / 100);
		
		if(temp < -80)
		{
			temp = -80;
		}
		
		soundControl[clipNum].setValue
		(temp);
	}
	
   /**
    * Returns whether current sound clip is still active or not
    * @return
    */
	public boolean isStillActive()
	{
		return soundClip[clipNum].isActive();
	}
	
   /**
    * Stops a sound clip if it needs to be stopped
    */
	public void stopClip()
	{
		soundClip[clipNum].stop();
		soundClip[clipNum].setMicrosecondPosition(0);
	}
	
   /**
    * Stops all clips of this type. Mainly for lifting sounds
    */
	public void stopAll()
	{
		for(int i = 0; i < soundClip.length; i++)
		{
			soundClip[i].stop();
			soundClip[i].setMicrosecondPosition(0);
		}
	}
	
   /**
    * Mainly for music used outside of normal gameplay, this
    * static method can be accessed anywhere by statically calling this
    * class in order to set the clips volume. Optimizes the code.
    * @param temp
    */
	public static void setMusicVolume(Clip temp)
	{	
		float temp2 = 40f * (float) Math.log10
				(FPSLauncher.musicVolumeLevel / 100);
		
		if(temp2 < -80)
		{
			temp2 = -80;
		}
		
		FPSLauncher.musicVolumeControl.setValue
		(temp2);
	}
}
