package io.github.surajkumar.client.sound;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundPlayer implements LineListener {

    public void playSound() {
        Clip audioClip;
        try {
            audioClip = playSound("game_background.wav");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        audioClip.start();
    }


    private long start = 0;

    public void playSoundEffect() {
        if ((System.currentTimeMillis() - start) > 1000) {
            start = System.currentTimeMillis();
            Clip audioClip;
            try {
                audioClip = playSound("shoot.wav");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            audioClip.start();
        }
    }

    private Clip playSound(String path) throws Exception {
        InputStream inputStream = SoundPlayer.class.getClassLoader().getResourceAsStream(path);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
        AudioFormat audioFormat = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.open(audioStream);
        return audioClip;
    }

    @Override
    public void update(LineEvent event) {

    }
}
