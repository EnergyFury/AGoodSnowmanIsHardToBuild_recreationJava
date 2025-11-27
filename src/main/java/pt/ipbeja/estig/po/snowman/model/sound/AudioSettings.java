/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.model.sound;

import static pt.ipbeja.estig.po.snowman.constants.Game.VOLUME_MUSIC;

public class AudioSettings {

    private static AudioSettings instance;
    private double volume;

    private AudioSettings() {

        this.volume = VOLUME_MUSIC;
    }

    public static synchronized AudioSettings getInstance() {
        if (instance == null) {
            instance = new AudioSettings();
        }
        return instance;
    }

    public void setMusicVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        VOLUME_MUSIC = this.volume;
    }
}
