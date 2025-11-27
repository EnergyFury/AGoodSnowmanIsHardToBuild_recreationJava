/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.constants;

import pt.ipbeja.estig.po.snowman.model.sound.SoundPlayer;

import static pt.ipbeja.estig.po.snowman.constants.Game.BACKGROUND_MUSIC_PATH;

public class Media {

    public static final SoundPlayer BACKGROUND_MUSIC = createBackgroundMusic();

    private Media() {

        throw new AssertionError("Esta classe não deve ser instanciada");
    }

    private static SoundPlayer createBackgroundMusic() {

        try {
            return new SoundPlayer(BACKGROUND_MUSIC_PATH);
        } catch (Exception e) {
            System.err.println("Aviso: Áudio desativado devido a erro: " + e.getMessage());
            return new SoundPlayer();
        }
    }
}
