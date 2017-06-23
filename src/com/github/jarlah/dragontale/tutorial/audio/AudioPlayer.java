package com.github.jarlah.dragontale.tutorial.audio;

import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingUtilities;

public class AudioPlayer {

    private Clip clip;
    private final String link;
    private final float volume;

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
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            throw new IllegalStateException("Could not load audio player", e);
        }
    }

    public boolean isPlaying() {
        if (clip == null) {
            return false;
        }
        synchronized (AudioPlayer.this) {
            return clip.isActive();
        }
    }

    public void play() {
        SwingUtilities.invokeLater(() -> {
            synchronized (AudioPlayer.this) {
                if (clip == null) {
                    init();
                }
                stop();
                clip.setFramePosition(0);
                clip.start();
            }
        });
    }

    public void stop() {
        if (clip == null) {
            return;
        }
        synchronized (AudioPlayer.class) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }

    public void close() {
        if (clip == null) {
            return;
        }
        synchronized (AudioPlayer.class) {
            stop();
            clip.close();
        }
    }

    public void playLoop() {
        SwingUtilities.invokeLater(() -> {
            synchronized (AudioPlayer.this) {
                if (clip == null) {
                    init();
                }
                stop();
                clip.setFramePosition(0);
                clip.setLoopPoints(0, clip.getFrameLength() - 10);
                clip.loop(-1);
            }
        });
    }
}
