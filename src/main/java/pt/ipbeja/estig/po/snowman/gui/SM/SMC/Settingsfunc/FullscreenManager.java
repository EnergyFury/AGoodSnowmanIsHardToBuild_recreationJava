/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pt.ipbeja.estig.po.snowman.model.enums.DisplayResolution;

public class FullscreenManager {

    private static final FullscreenManager instance = new FullscreenManager();
    private final GameSettings settings;
    private Stage stage;
    private Scene scene;

    private FullscreenManager() {
        this.settings = GameSettings.getInstance();
    }

    public static FullscreenManager getInstance() {
        return instance;
    }

    public void initialize(Stage stage, Scene scene) {

        this.stage = stage;
        this.scene = scene;

        configureFullscreen();
        setupFullscreenControls();
    }

    public void configureFullscreen() {

        if (stage != null) {
            boolean isFullscreen = settings.isFullscreen();

            stage.setFullScreen(isFullscreen);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        }
    }

    private void setupFullscreenControls() {

        if (scene != null) {

            scene.setOnKeyPressed(event -> {

                if (event.getCode() == KeyCode.F11) {

                    toggleFullscreen();
                }
            });
        }
    }

    public void toggleFullscreen() {

        if (stage != null) {

            boolean newFullscreenState = !stage.isFullScreen();
            setFullscreen(newFullscreenState);
        }
    }

    public void setFullscreen(boolean fullscreen) {

        if (stage != null) {

            settings.setFullscreen(fullscreen);
            stage.setFullScreen(fullscreen);

            if (!fullscreen) {

                // Restaura a resolução quando sair do modo tela cheia
                DisplayResolution currentResolution = settings.getResolution();
                stage.setWidth(currentResolution.getWidth());
                stage.setHeight(currentResolution.getHeight());
                centerOnCurrentScreen();
            }
        }
    }

    public void centerOnCurrentScreen() {

        if (stage != null) {

            Screen currentScreen = Screen.getScreens().get(settings.getSelectedScreenIndex());

            double centerX = currentScreen.getBounds().getMinX() +
                    (currentScreen.getBounds().getWidth() - stage.getWidth()) / 2;
            double centerY = currentScreen.getBounds().getMinY() +
                    (currentScreen.getBounds().getHeight() - stage.getHeight()) / 2;

            stage.setX(centerX);
            stage.setY(centerY);
        }
    }

    public void updateScene(Scene newScene) {

        this.scene = newScene;

        setupFullscreenControls();
    }
}