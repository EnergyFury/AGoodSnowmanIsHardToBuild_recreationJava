/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.gui.SM.SMC;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;

import static pt.ipbeja.estig.po.snowman.constants.DefaultStyle.DEFAULT_BACKGROUND;
import static pt.ipbeja.estig.po.snowman.constants.Game.*;

public class Credits extends VBox {

    public Credits(Stage primaryStage, Runnable backToMenu) {

        Font customFont;
        try (InputStream fontStream = getClass().getResourceAsStream(FONT_01)) {
            if (fontStream == null) {
                System.err.println("Aviso: recurso de fonte não encontrado no classpath em " + FONT_01 + ". " +
                        "Certifica-te de que o ficheiro existe (ex.: src/main/resources" + FONT_PATH + "Signika-Bold.ttf). " +
                        "Usando fonte padrão.");
                customFont = Font.font(FONT_01_SIZE);
            } else {
                Font loaded = Font.loadFont(fontStream, FONT_01_SIZE);
                if (loaded == null) {
                    System.err.println("Aviso: falha ao carregar a fonte (" + FONT_01 + "). Usando fonte padrão.");
                    customFont = Font.font(FONT_01_SIZE);
                } else {
                    customFont = loaded;
                }
            }
        } catch (Exception ex) {
            System.err.println("Aviso: erro ao carregar fonte (" + FONT_01 + "): " + ex.getMessage());
            customFont = Font.font(FONT_01_SIZE);
        }

        this.setBackground(DEFAULT_BACKGROUND);

        Label title = new Label("Créditos");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        title.setFont(customFont);

        Label devs = new Label("Desenvolvido por:");
        devs.setStyle("-fx-text-fill: white;");
        devs.setFont(customFont);
        devs.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label devsLabel = new Label("[José Cabaço - 26906]\n[Juliano Santos - 27133]");
        devsLabel.setStyle("-fx-text-fill: white;");
        devsLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label rGraphic = new Label("Recursos gráficos:");
        rGraphic.setStyle("-fx-text-fill: white;");
        rGraphic.setFont(customFont);
        rGraphic.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label rGraphicLabel = new Label("[Fonte dos gráficos, se aplicável]");
        rGraphicLabel.setStyle("-fx-text-fill: white;");
        rGraphicLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label music = new Label("Música:");
        music.setStyle("-fx-text-fill: white;");
        music.setFont(customFont);
        music.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label musicLabel = new Label("[Fonte da música, se aplicável]");
        musicLabel.setStyle("-fx-text-fill: white;");
        musicLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Back Button
        Button backButton = new Button("Voltar");
        backButton.setOnAction(e -> backToMenu.run());
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        backButton.setFont(customFont);

        backButton.setOnMouseEntered(e -> {
            backButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white;");
        });

        backButton.setOnMouseExited(e -> {
            backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        });

        backButton.setMinWidth(DEFAULT_BUTTON_WIDTH);

        this.getChildren().addAll(
                title, devs, devsLabel, rGraphic, rGraphicLabel, music, musicLabel, backButton);

        this.setAlignment(Pos.CENTER);

        this.setSpacing(DEFAULT_SPACING);

        this.setPadding(new Insets(DEFAULT_PADDING));

    }
}