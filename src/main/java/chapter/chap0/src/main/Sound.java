package chapter.chap0.src.main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/little-town-orchestral.wav");
        soundURL[1] = getClass().getResource("/sound/coin.wav");
        soundURL[2] = getClass().getResource("/sound/powerup.wav");
        soundURL[3] = getClass().getResource("/sound/unlock.wav");
        soundURL[4] = getClass().getResource("/sound/fanfare.wav");
        soundURL[5] = getClass().getResource("/sound/hitmonster.wav");
        soundURL[6] = getClass().getResource("/sound/receivedamage.wav");
        soundURL[7] = getClass().getResource("/sound/levelup.wav");
        soundURL[8] = getClass().getResource("/sound/cursor.wav");
        soundURL[9] = getClass().getResource("/sound/burning.wav");
        soundURL[10] = getClass().getResource("/sound/gameover.wav");
        soundURL[11] = getClass().getResource("/sound/stairs.wav");
        soundURL[12] = getClass().getResource("/sound/8bit-Bossa.wav");
        soundURL[13] = getClass().getResource("/sound/MoveCar.wav");
        soundURL[14] = getClass().getResource("/sound/race.wav");
        soundURL[15] = getClass().getResource("/sound/vacham.wav");
        soundURL[16] = getClass().getResource("/sound/Woodland-Fantasy.wav");
        soundURL[17] = getClass().getResource("/sound/among.wav");
        soundURL[18] = getClass().getResource("/sound/nature sketch.wav");
    }

    public void setFile(int i) {

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
        } catch (Exception e) {

        }

    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null && clip.isOpen()) {
            clip.stop();
            clip.close();
        }
    }

    public void checkVolume() {
        switch (volumeScale) {
            case 0: volume = -80f;break;
            case 1: volume = -20f;break;
            case 2: volume = -12f;break;
            case 3: volume = -5f;break;
            case 4: volume = 1f;break;
            case 5: volume = 6f;break;
        }
        fc.setValue(volume);
    }

}
