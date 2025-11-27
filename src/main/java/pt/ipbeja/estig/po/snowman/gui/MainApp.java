/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pt.ipbeja.estig.po.snowman.gui.SM.StartMenu;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc.FullscreenManager;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc.GameSettings;
import pt.ipbeja.estig.po.snowman.model.sound.SoundPlayer;

import java.util.List;

import static pt.ipbeja.estig.po.snowman.constants.Game.COMPLETE_GAME_TITLE;
import static pt.ipbeja.estig.po.snowman.constants.Media.BACKGROUND_MUSIC;

/**
 * Classe principal para iniciar a aplicação.
 */

public class MainApp extends Application {
    private GameSettings settings;

    @Override
    public void start(Stage primaryStage) {
        initializeSettings();
        setupAndShowStage(primaryStage);
    }

    private void initializeSettings() {
        this.settings = GameSettings.getInstance();
    }

    private Screen getTargetScreen() {
        List<Screen> screens = Screen.getScreens();
        int savedScreenIndex = settings.getSelectedScreenIndex();

        if (savedScreenIndex >= 0 && savedScreenIndex < screens.size()) {
            return screens.get(savedScreenIndex);
        }
        return Screen.getPrimary();
    }

    private StartMenu createStartMenu(Stage primaryStage) {
        return new StartMenu(primaryStage, BACKGROUND_MUSIC, settings.getResolution());
    }

    private Scene createMainScene(StartMenu startMenu) {
        return new Scene(
                startMenu,
                settings.getResolution().getWidth(),
                settings.getResolution().getHeight()
        );
    }

    private void configureStage(Stage primaryStage, Scene scene) {
        primaryStage.setTitle(COMPLETE_GAME_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setWidth(settings.getResolution().getWidth());
        primaryStage.setHeight(settings.getResolution().getHeight());
        primaryStage.setResizable(true);
    }

    private void centerStageOnScreen(Stage primaryStage, Screen targetScreen) {
        double centerX = targetScreen.getBounds().getMinX() +
                (targetScreen.getBounds().getWidth() - primaryStage.getWidth()) / 2;
        double centerY = targetScreen.getBounds().getMinY() +
                (targetScreen.getBounds().getHeight() - primaryStage.getHeight()) / 2;
        primaryStage.setX(centerX);
        primaryStage.setY(centerY);
    }

    private void setupAndShowStage(Stage primaryStage) {
        FullscreenManager fullscreenManager = FullscreenManager.getInstance();

        Screen targetScreen = getTargetScreen();
        StartMenu startMenu = createStartMenu(primaryStage);
        Scene startScene = createMainScene(startMenu);

        configureStage(primaryStage, startScene);
        centerStageOnScreen(primaryStage, targetScreen);

        fullscreenManager.initialize(primaryStage, startScene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}