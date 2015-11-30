package com.github.jarlah.dragontale.tutorial.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.SwingUtilities;

public class AudioPlayer {
	private Clip clip;
	private String link;
	private float volume;

	public AudioPlayer(String link, float volume) {
		this.link = link;
		this.volume = volume;
	}

	private void init() {
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
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(volume);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isPlaying() {
		synchronized(this){
			return clip.isActive();
		}
	}
	
	public void play() {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	synchronized(this) {
		    		if (clip == null) init();
		    		stop();
			    	clip.setFramePosition(0);
			    	clip.start();
		    	}
		    	
		    }
		 });
	}

	public void stop() {
		synchronized(this){
			if (clip.isRunning()) clip.stop();
		}
	}
	
	public void close() {
		synchronized(this){
			stop();
			clip.close();
		}
	}

	public void playLoop() {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	synchronized(this) {
		    		if (clip == null) init();
					stop();
					clip.setFramePosition(0);
					clip.setLoopPoints(0, clip.getFrameLength()-10);
					clip.loop(-1);
		    	}
		    }
		 });
	}
}
