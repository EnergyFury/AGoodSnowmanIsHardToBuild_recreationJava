package pt.ipbeja.estig.po.snowman.gui.SM;

/***
 * Nome:         Numero:
 * José Cabaço    26906
 * */

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pt.ipbeja.estig.po.snowman.gui.GP.GSM.GameStateManager;
import pt.ipbeja.estig.po.snowman.gui.GP.GamePanel;
import pt.ipbeja.estig.po.snowman.gui.NID.NameInputDialog;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Credits;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settings;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc.FullscreenManager;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc.GameSettings;
import pt.ipbeja.estig.po.snowman.model.Board.BoardModel;
import pt.ipbeja.estig.po.snowman.model.sound.SoundPlayer;
import pt.ipbeja.estig.po.snowman.model.enums.DisplayResolution;

import java.io.InputStream;
import java.util.Objects;

import static pt.ipbeja.estig.po.snowman.constants.DefaultStyle.DEFAULT_BACKGROUND;
import static pt.ipbeja.estig.po.snowman.constants.Game.*;

public class StartMenu extends StackPane {

    private final DisplayResolution initialResolution;

    public StartMenu(Stage primaryStage, SoundPlayer backgroundMusic, DisplayResolution initialResolution) {
        this.initialResolution = initialResolution;

        GameSettings settings = GameSettings.getInstance();

        Font customFont = loadCustomFont();

        this.setBackground(DEFAULT_BACKGROUND);

        Button playButton = createMenuButton("Jogar", customFont);
        Button settingsButton = createMenuButton("Definições", customFont);
        Button creditsButton = createMenuButton("Créditos", customFont);
        Button quitButton = createMenuButton("Sair", customFont);

        backgroundMusic.play();

        ImageView logoView = createImageView(MENUIMAGE_LOGO);
        ImageView snowmanView = createImageView("/background/snowman.png");

        GridPane backgroundGrid = buildBackgroundGrid(logoView, snowmanView,
                new VBox(20, playButton, settingsButton, creditsButton, quitButton));

        this.getChildren().add(backgroundGrid);

        attachActions(primaryStage, backgroundMusic, playButton, settingsButton, creditsButton, quitButton);
    }

    private Font loadCustomFont() {
        try (InputStream fontStream = getClass().getResourceAsStream(FONT_01)) {
            if (fontStream == null) {
                System.err.println("Aviso: recurso de fonte não encontrado no classpath em " + FONT_01 + ". Usando fonte padrão.");
                return Font.font(FONT_01_SIZE);
            }
            Font loaded = Font.loadFont(fontStream, FONT_01_SIZE);
            if (loaded == null) {
                System.err.println("Aviso: falha ao carregar a fonte (" + FONT_01 + "). Usando fonte padrão.");
                return Font.font(FONT_01_SIZE);
            }
            return loaded;
        } catch (Exception ex) {
            System.err.println("Aviso: erro ao carregar fonte (" + FONT_01 + "): " + ex.getMessage());
            return Font.font(FONT_01_SIZE);
        }
    }

    private Button createMenuButton(String text, Font font) {
        Button button = new Button(text);
        button.setMinWidth(DEFAULT_BUTTON_WIDTH);
        button.setFont(font);
        setTransparentHoverStyle(button);
        return button;
    }

    private void setTransparentHoverStyle(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white;"));
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
    }

    private ImageView createImageView(String resourcePath) {
        Image image = new Image(Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm());
        ImageView view = new ImageView(image);
        view.setPreserveRatio(true);
        return view;
    }

    private GridPane buildBackgroundGrid(ImageView logoView, ImageView snowmanView, VBox buttonBox) {
        GridPane backgroundGrid = new GridPane();
        backgroundGrid.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setPercentWidth(33.33);
        col2.setPercentWidth(33.33);
        col3.setPercentWidth(33.33);
        backgroundGrid.getColumnConstraints().addAll(col1, col2, col3);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        row1.setPercentHeight(50);
        row2.setPercentHeight(50);
        backgroundGrid.getRowConstraints().addAll(row1, row2);

        logoView.fitWidthProperty().bind(backgroundGrid.widthProperty().multiply(0.3));
        snowmanView.fitHeightProperty().bind(backgroundGrid.heightProperty().multiply(0.3));

        buttonBox.setAlignment(Pos.CENTER);

        GridPane.setHalignment(logoView, HPos.CENTER);
        GridPane.setValignment(logoView, VPos.CENTER);
        backgroundGrid.add(logoView, 1, 0);

        GridPane.setHalignment(buttonBox, HPos.CENTER);
        GridPane.setValignment(buttonBox, VPos.CENTER);
        backgroundGrid.add(buttonBox, 1, 1);

        GridPane.setHalignment(snowmanView, HPos.RIGHT);
        GridPane.setValignment(snowmanView, VPos.CENTER);
        backgroundGrid.add(snowmanView, 2, 1);

        return backgroundGrid;
    }

    private void attachActions(Stage primaryStage, SoundPlayer backgroundMusic,
                               Button playButton, Button settingsButton,
                               Button creditsButton, Button quitButton) {

        playButton.setOnAction(e -> {
            BoardModel boardModel = new BoardModel(DEFAULT_ROWS, DEFAULT_COLS);
            GameStateManager gameStateManager = new GameStateManager(primaryStage, boardModel, backgroundMusic);
            GamePanel gamePanel = new GamePanel(gameStateManager);

            NameInputDialog.showEmbedded(this, name -> {
                gamePanel.setPlayerName(name);
                switchScene(primaryStage, new Scene(gamePanel));
                gamePanel.requestFocus();
            });
        });

        settingsButton.setOnAction(e -> {
            Settings settingsPanel = new Settings(primaryStage, backgroundMusic, () -> {
                StartMenu menu = new StartMenu(primaryStage, backgroundMusic, this.initialResolution);
                switchScene(primaryStage, new Scene(menu));
            });
            switchScene(primaryStage, new Scene(settingsPanel));
        });

        creditsButton.setOnAction(e -> {
            Credits creditsPanel = new Credits(primaryStage, () -> {
                StartMenu menu = new StartMenu(primaryStage, backgroundMusic, this.initialResolution);
                switchScene(primaryStage, new Scene(menu));
            });
            switchScene(primaryStage, new Scene(creditsPanel));
        });

        quitButton.setOnAction(e -> {
            backgroundMusic.stop();
            primaryStage.close();
        });
    }

    private void switchScene(Stage stage, Scene scene) {
        FullscreenManager fullscreenManager = FullscreenManager.getInstance();
        fullscreenManager.updateScene(scene);
        stage.setScene(scene);
        fullscreenManager.configureFullscreen();
    }
}