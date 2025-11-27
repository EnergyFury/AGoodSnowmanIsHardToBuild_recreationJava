/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.gui.SM.SMC;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pt.ipbeja.estig.po.snowman.model.sound.SoundPlayer;
import pt.ipbeja.estig.po.snowman.model.enums.DisplayResolution;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc.GameSettings;
import pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc.FullscreenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pt.ipbeja.estig.po.snowman.constants.DefaultStyle.DEFAULT_BACKGROUND;
import static pt.ipbeja.estig.po.snowman.constants.Game.*;

public class Settings extends VBox {

    private final Stage primaryStage;
    private final GameSettings settings;
    private final Button fullScreenButton;
    private final ComboBox<DisplayResolution> resolutionComboBox;
    private final ComboBox<Screen> screenComboBox;

    public Settings(Stage primaryStage, SoundPlayer backgroundMusic, Runnable backToMenu) {
        this.primaryStage = primaryStage;
        this.settings = GameSettings.getInstance();

        FullscreenManager fullscreenManager = FullscreenManager.getInstance();

        this.setBackground(DEFAULT_BACKGROUND);
        this.setSpacing(DEFAULT_SPACING);
        this.setPadding(new Insets(DEFAULT_PADDING));
        this.setAlignment(Pos.CENTER);

        Font customFont = Font.loadFont(Objects.requireNonNull(getClass().getResource(
                FONT_01)).toExternalForm(),
                FONT_01_SIZE);

        Label title = new Label("Definições");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        // Resolution
        Label resolutionLabel = new Label("Resolução:");
        resolutionLabel.setStyle("-fx-text-fill: white;");
        resolutionLabel.setFont(customFont);

        resolutionComboBox = new ComboBox<>();
        resolutionComboBox.setOnAction(event -> {
            DisplayResolution selectedResolution = resolutionComboBox.getValue();
            settings.setResolution(selectedResolution);
            if (selectedResolution != DisplayResolution.R_1920x1080) {
                int width = selectedResolution.getWidth();
                int height = selectedResolution.getHeight();
                if (width > 0 && height > 0) {
                    primaryStage.setFullScreen(false);
                    primaryStage.setWidth(width);
                    primaryStage.setHeight(height);
                    centerStageOnScreen(primaryStage, getCurrentScreen());
                }
            }
        });

        HBox resolutionBox = new HBox(DEFAULT_SPACING, resolutionLabel, resolutionComboBox);
        resolutionBox.setAlignment(Pos.CENTER);

        // Output Monitor
        Label screenTypeLabel = new Label("Trocar Monitor:");
        screenTypeLabel.setStyle("-fx-text-fill: white;");
        screenTypeLabel.setFont(customFont);

        screenComboBox = new ComboBox<>();
        List<Screen> screens = Screen.getScreens();
        screenComboBox.getItems().addAll(screens);

        screenComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Screen screen) {
                int index = screens.indexOf(screen);
                return "" + (index + 1);
            }

            @Override
            public Screen fromString(String string) {
                return null;
            }
        });

        Screen currentScreen = getCurrentScreen();
        screenComboBox.setValue(currentScreen);
        settings.setSelectedScreenIndex(Screen.getScreens().indexOf(currentScreen));

        updateResolutionsForScreen(currentScreen);

        screenComboBox.setOnAction(event -> {
            Screen selectedScreen = screenComboBox.getValue();
            if (selectedScreen != null) {
                updateResolutionsForScreen(selectedScreen);
                settings.setSelectedScreenIndex(Screen.getScreens().indexOf(selectedScreen));
                if (!primaryStage.isFullScreen()) {
                    centerStageOnScreen(primaryStage, selectedScreen);
                }
            }
        });

        HBox screenTypeBox = new HBox(DEFAULT_SPACING, screenTypeLabel, screenComboBox);
        screenTypeBox.setAlignment(Pos.CENTER);

        // FullScreen ON/OFF
        fullScreenButton = new Button("FullScreen: " + (settings.isFullscreen() ? "ON" : "OFF"));
        fullScreenButton.setFont(customFont);
        fullScreenButton.setOnAction(event -> {
            fullscreenManager.toggleFullscreen();
            fullScreenButton.setText("FullScreen: " + (settings.isFullscreen() ? "ON" : "OFF"));

        });

        // Adicionar controle de volume
        Label volumeLabel = new Label("Volume:");
        volumeLabel.setStyle("-fx-text-fill: white;");
        volumeLabel.setFont(customFont);

        Slider volumeSlider = new Slider(0, 1, settings.getMusicVolume());
        volumeSlider.setOrientation(Orientation.HORIZONTAL);
        volumeSlider.setPrefWidth(100);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            settings.setMusicVolume(newVal.doubleValue());
            backgroundMusic.setVolume(newVal.doubleValue());
        });

        HBox volumeBox = new HBox(DEFAULT_SPACING, volumeLabel, volumeSlider);
        volumeBox.setAlignment(Pos.CENTER);

        // Back Button
        Button backButton = getButton(backToMenu, customFont);

        this.getChildren().addAll(title, resolutionBox, screenTypeBox,
                fullScreenButton, volumeBox, backButton);

    }

    private static Button getButton(Runnable backToMenu, Font customFont) {
        Button backButton = new Button("Voltar");
        backButton.setOnAction(e -> backToMenu.run());
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        backButton.setFont(customFont);
        backButton.setMinWidth(DEFAULT_BUTTON_WIDTH);

        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white;"));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white;"));
        return backButton;
    }

    private Screen getCurrentScreen() {
        for (Screen screen : Screen.getScreens()) {
            if (primaryStage.getX() >= screen.getBounds().getMinX() &&
                    primaryStage.getX() < screen.getBounds().getMaxX() &&
                    primaryStage.getY() >= screen.getBounds().getMinY() &&
                    primaryStage.getY() < screen.getBounds().getMaxY()) {
                return screen;
            }
        }
        return Screen.getPrimary();
    }

    private void centerStageOnScreen(Stage stage, Screen screen) {
        double centerX = screen.getBounds().getMinX() + (screen.getBounds().getWidth() - stage.getWidth()) / 2;
        double centerY = screen.getBounds().getMinY() + (screen.getBounds().getHeight() - stage.getHeight()) / 2;
        stage.setX(centerX);
        stage.setY(centerY);
    }

    private void updateResolutionsForScreen(Screen screen) {
        List<DisplayResolution> compatibleResolutions = getCompatibleResolutions(screen);
        resolutionComboBox.getItems().setAll(compatibleResolutions);

        DisplayResolution savedResolution = settings.getResolution();
        if (compatibleResolutions.contains(savedResolution)) {
            resolutionComboBox.setValue(savedResolution);
        } else {
            resolutionComboBox.setValue(GameSettings.getInstance().getResolution());
        }
    }

    private List<DisplayResolution> getCompatibleResolutions(Screen screen) {
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        List<DisplayResolution> compatible = new ArrayList<>();
        for (DisplayResolution resolution : DisplayResolution.values()) {
            if (resolution == DisplayResolution.R_1920x1080 ||
                    (resolution.getWidth() <= screenWidth && resolution.getHeight() <= screenHeight)) {
                compatible.add(resolution);
            }
        }
        return compatible;
    }
}