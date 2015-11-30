package com.github.jarlah.dragontale.tutorial.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioPlayer {
	private Clip clip;

	public AudioPlayer(String link, float volume) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream(link));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			if (volume != -1) {
			FloatControl gainControl = 
				    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(volume);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isPlaying() {
		return clip.isActive();
	}
	
	public void play() {
		if (clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}

	public void stop() {
		if (clip.isRunning()) clip.stop();
	}
	
	public void close() {
		stop();
		clip.close();
	}

	public void playLoop() {
		if (clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.setLoopPoints(0, clip.getFrameLength()-10);
		clip.loop(-1);
	}
}
