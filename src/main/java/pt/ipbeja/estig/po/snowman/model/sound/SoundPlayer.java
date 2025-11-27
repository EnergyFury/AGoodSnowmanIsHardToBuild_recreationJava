/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.model.sound;

import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc.GameSettings;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.io.BufferedInputStream;
import java.io.InputStream;

import static pt.ipbeja.estig.po.snowman.constants.Game.MUSIC_PATH;
import static pt.ipbeja.estig.po.snowman.constants.Game.VOLUME_MUSIC;

public class SoundPlayer {

    private Clip clip;
    private FloatControl volumeControl;
    private boolean isValid;

    public SoundPlayer(String soundFile) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(MUSIC_PATH + soundFile);
            if (audioSrc == null) {
                throw new RuntimeException("Arquivo de áudio não encontrado: " + soundFile);
            }

            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume(VOLUME_MUSIC);
            isValid = true;
        } catch (Exception e) {
            System.err.println("Erro ao inicializar áudio: " + e.getMessage());
            isValid = false;
        }
    }

    public SoundPlayer() {
        this.isValid = false;
    }

    public void play() {
        if (isValid && clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (isValid && clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (isValid && clip != null) {
            clip.stop();
        }
    }

    public void pause() {
        if (isValid && clip != null) {
            clip.stop();
        }
    }


    public void setVolume(double volume) {
        if (isValid && volumeControl != null) {
            volume = Math.max(0, Math.min(1, volume));
            float dB = (float) (Math.log10(volume) * 20.0f);
            volumeControl.setValue(Math.max(volumeControl.getMinimum(),
                    Math.min(volumeControl.getMaximum(), dB)));
            AudioSettings.getInstance().setMusicVolume(volume);
            VOLUME_MUSIC = volume;
            GameSettings.getInstance().setMusicVolume(volume);
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public void close() {
        if (clip != null) {
            clip.close();
        }
    }
}
