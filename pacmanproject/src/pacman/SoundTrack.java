package pacman;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundTrack {
	Clip clip;
	URL soundURL[] = new URL[40];
	
	public SoundTrack() {
		soundURL[0] = getClass().getResource("/SoundTrack/PacManTheme.wav");
		soundURL[1] = getClass().getResource("/SoundTrack/waka.wav");
		soundURL[2] = getClass().getResource("/SoundTrack/gameOver.wav");
	}
	
	public void setFile(int i) {
		try {
			AudioInputStream aud = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(aud);
		}catch(Exception e) {
			
		}
	}
	public void play() {
		clip.start();
		
	}
	public void loop() {
		clip.loop(clip.LOOP_CONTINUOUSLY);
	}
	public void stop() {
	    if (clip != null) {
	        clip.stop();
	    }
	}
	
}
