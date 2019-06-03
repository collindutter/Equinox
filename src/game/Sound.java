package game;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public enum Sound
{
	CLICK("componentclick.wav"),
	SHOOT("firesound.wav"),
	GRAPPLE("grapplesound.wav"),
	BUTTON_HOVER("buttonhover.wav"),
	ENTITY_HIT("entityhit.wav"),
	MASS_ABSORB("masspickup.wav"),
	POWERUP_ABSORB("poweruppickup.wav"),
	MASS_TAKE("masstaken.wav");
	
	private Clip clip;

	Sound(String soundFileName)
	{
		try
		{
			URL url = this.getClass().getClassLoader().getResource("sounds/" + soundFileName);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		}catch(UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}catch(LineUnavailableException e)
		{
			e.printStackTrace();
		}
	}
	
	public void play()
	{
		if (Game.settings.get("sound"))
		{
			if (clip.isRunning())
				clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
	}

	// Optional method to pre-load all the sound files.
	public static void init()
	{
		values();
	}
}