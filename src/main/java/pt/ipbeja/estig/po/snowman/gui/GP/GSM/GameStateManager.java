/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.gui.GP.GSM;

import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.ipbeja.estig.po.snowman.gui.PM.PauseMenu;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc.FullscreenManager;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc.GameSettings;
import pt.ipbeja.estig.po.snowman.gui.SM.StartMenu;
import pt.ipbeja.estig.po.snowman.model.Board.BoardModel;
import pt.ipbeja.estig.po.snowman.model.sound.SoundPlayer;

import static pt.ipbeja.estig.po.snowman.constants.Media.BACKGROUND_MUSIC;

public class GameStateManager {

    private boolean isPaused;
    private final Stage primaryStage;
    private PauseMenu pauseMenu;

    public GameStateManager(Stage primaryStage, BoardModel boardModel, SoundPlayer backgroundMusic) {

        this.primaryStage = primaryStage;
        this.isPaused = false;
    }

    public void togglePause() {

        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    public void pauseGame() {

        BACKGROUND_MUSIC.pause();
        isPaused = true;

        if (pauseMenu == null) {
            pauseMenu = new PauseMenu(primaryStage, this, BACKGROUND_MUSIC);
        }

        pauseMenu.show();
    }

    public void resumeGame() {

        BACKGROUND_MUSIC.play();
        isPaused = false;
    }

    public void returnToMainMenu() {

        StartMenu startMenu = new StartMenu(primaryStage, BACKGROUND_MUSIC,
                GameSettings.getInstance().getResolution());
        Scene startScene = new Scene(startMenu);
        primaryStage.setScene(startScene);

        FullscreenManager fullscreenManager = FullscreenManager.getInstance();
        fullscreenManager.updateScene(startScene);

        fullscreenManager.configureFullscreen();
    }
}
