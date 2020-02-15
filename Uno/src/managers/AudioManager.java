package managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
	private static ArrayList<Clip> clips = new ArrayList<Clip>();
	private static HashMap<String, File> soundList = new HashMap<String, File>();;
	public static boolean SOUND;

	public AudioManager() {
		soundList.put("click", new File("src/sounds/click.wav"));
		soundList.put("swipe", new File("src/sounds/swipe.wav"));
		soundList.put("win", new File("src/sounds/win.wav"));
		soundList.put("lose", new File("src/sounds/lose.wav"));
		soundList.put("draw-0", new File("src/sounds/draw-0.wav"));
		soundList.put("draw-1", new File("src/sounds/draw-1.wav"));
		soundList.put("draw-2", new File("src/sounds/draw-2.wav"));
		soundList.put("draw-3", new File("src/sounds/draw-3.wav"));
		soundList.put("play-0", new File("src/sounds/play-0.wav"));
		soundList.put("play-1", new File("src/sounds/play-1.wav"));
		soundList.put("play-2", new File("src/sounds/play-2.wav"));
		soundList.put("play-3", new File("src/sounds/play-3.wav"));
		soundList.put("play-4", new File("src/sounds/play-4.wav"));
		soundList.put("play-5", new File("src/sounds/play-5.wav"));
		soundList.put("play-6", new File("src/sounds/play-6.wav"));
		soundList.put("play-7", new File("src/sounds/play-7.wav"));
	}

	public static Clip playSound(String key) {
		if (key.equals("draw"))
			key += "-" + (int) (Math.random() * 4);
		else if (key.equals("play"))
			key += "-" + (int) (Math.random() * 8);
		if (SOUND && soundList.containsKey(key))
			try {
				Clip clip = AudioSystem.getClip();
				clips.add(clip);
				clip.open(AudioSystem.getAudioInputStream(soundList.get(key)));
				clip.addLineListener(new LineListener() {
					@Override
					public void update(LineEvent event) {
						if (event.getType() == LineEvent.Type.STOP)
							clips.remove(((Clip) event.getSource()));
					}
				});
				clip.start();
				return clip;
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
				e.printStackTrace();
			}
		return null;
	}

	public static void refresh() {
		if (!SOUND)
			stopClips();
	}

	private static void stopClips() {
		while (clips.size() > 0)
			clips.remove(0).stop();
	}
}