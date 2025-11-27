/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.gui.PM;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.ipbeja.estig.po.snowman.gui.GP.GSM.GameStateManager;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settings;
import pt.ipbeja.estig.po.snowman.model.sound.SoundPlayer;

import static pt.ipbeja.estig.po.snowman.constants.DefaultStyle.DEFAULT_BACKGROUND;
import static pt.ipbeja.estig.po.snowman.constants.Game.*;
import static pt.ipbeja.estig.po.snowman.constants.Media.BACKGROUND_MUSIC;

public class PauseMenu extends VBox {

    private Scene pauseScene;
    private final Stage pauseStage;
    private final GameStateManager gameStateManager;

    public PauseMenu(Stage primaryStage, GameStateManager gameStateManager, SoundPlayer backgroundMusic) {

        this.setBackground(DEFAULT_BACKGROUND);

        this.gameStateManager = gameStateManager;
        this.pauseStage = new Stage(StageStyle.UNDECORATED);
        this.pauseStage.initModality(Modality.APPLICATION_MODAL);
        this.pauseStage.initOwner(primaryStage);
        this.pauseStage.setTitle(PAUSEMENU_TITLE);

        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(DEFAULT_PADDING));

        createContent();

        this.pauseScene = new Scene(
                this,
                PAUSE_MENU_WIDTH,
                PAUSE_MENU_HEIGHT);

        this.pauseStage.setScene(pauseScene);
        this.pauseStage.setResizable(false);
    }

    private void createContent() {

        Label titleLabel = new Label("Jogo Pausado");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Button resumeButton = new Button("Continuar");
        Button settingsButton = new Button("Definições");
        Button mainMenuButton = new Button("Voltar ao Menu");

        styleButtons(resumeButton, settingsButton, mainMenuButton);

        resumeButton.setOnAction(e -> hide());
        settingsButton.setOnAction(e -> showSettings());
        mainMenuButton.setOnAction(e -> returnToMainMenu());

        // Adiciona os elementos a janela
        this.getChildren().addAll(
                titleLabel,
                resumeButton,
                settingsButton,
                mainMenuButton
        );
    }

    private void styleButtons(Button... buttons) {

        for (Button button : buttons) {
            button.setMinWidth(DEFAULT_BUTTON_WIDTH);
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white;");

            // Hover
            button.setOnMouseEntered(e ->
                    button.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: white;"));
            button.setOnMouseExited(e ->
                    button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white;"));
        }
    }

    public void show() {
        pauseStage.show();
        pauseStage.centerOnScreen();
    }

    public void hide() {
        pauseStage.hide();
        gameStateManager.resumeGame();
    }

    private void showSettings() {

        Settings settings = new Settings(
                pauseStage,
                BACKGROUND_MUSIC,
                this::returnToPauseMenu
        );

        Scene settingsScene = new Scene(
                settings,
                PAUSE_MENU_WIDTH,
                PAUSE_MENU_HEIGHT);

        pauseStage.setScene(settingsScene);
        pauseStage.sizeToScene();

    }

    private void returnToPauseMenu() {
        pauseStage.setScene(pauseScene);
        pauseStage.sizeToScene();
        pauseStage.centerOnScreen();
    }


    private void returnToMainMenu() {
        hide();
        gameStateManager.returnToMainMenu();
    }
}