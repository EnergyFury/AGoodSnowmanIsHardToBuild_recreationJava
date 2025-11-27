package pt.ipbeja.estig.po.snowman.gui.NID;

/***
 * Nome:         Numero:
 * José Cabaço    26906
 * */

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.function.Consumer;

import static pt.ipbeja.estig.po.snowman.constants.DefaultStyle.DEFAULT_POPUPBACKGROUND;
import static pt.ipbeja.estig.po.snowman.constants.Game.*;

public class NameInputDialog {

    private static final String BTN_STYLE =
            "-fx-background-color: #1D1916FF;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 8px 16px;" +
                    "-fx-background-radius: 5px;";

    private static final String BTN_STYLE_HOVER =
            "-fx-background-color: #2E2827FF;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 8px 16px;" +
                    "-fx-background-radius: 5px;";

    public static void askName(Stage owner, Consumer<String> onNameEntered) {

        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nome do Jogador");

        TextField nameField = createNameField();
        Button okButton = createStyledButton("OK", true);

        addNameFilter(nameField, okButton);

        okButton.setOnAction(e -> {
            onNameEntered.accept(sanitizeName(nameField.getText()));
            dialog.close();
        });

        VBox layout = buildForm(nameField, okButton);

        Scene scene = new Scene(layout, 300, 200);
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.show();
        nameField.requestFocus();
    }

    public static void showEmbedded(StackPane parent, Consumer<String> onNameEntered) {
        TextField nameField = createNameField();
        Button okButton = createStyledButton("OK", true);     // começa desativado
        Button backButton = createStyledButton("Voltar", false); // ATIVO por padrão

        addNameFilter(nameField, okButton);

        VBox layout = buildForm(nameField, okButton, backButton);

        StackPane overlay = new StackPane(layout);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPickOnBounds(false);

        double startX = parent.getWidth() > 0 ? parent.getWidth() : 800;
        layout.setTranslateX(startX);

        if (!parent.getChildren().contains(overlay)) {
            parent.getChildren().add(overlay);
        }

        slide(layout, startX, 0, 350).setOnFinished(ev -> nameField.requestFocus());

        okButton.setOnAction(e -> {
            onNameEntered.accept(sanitizeName(nameField.getText()));
            slide(layout, 0, -parent.getWidth(), 250).setOnFinished(ev -> parent.getChildren().remove(overlay));
        });

        backButton.setOnAction(e -> {
            slide(layout, 0, parent.getWidth(), 250).setOnFinished(ev -> parent.getChildren().remove(overlay));
        });
    }

    private static TextField createNameField() {
        TextField nameField = new TextField();
        nameField.setMaxWidth(45);
        nameField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 8px;" +
                        "-fx-background-radius: 5px;"
        );
        return nameField;
    }

    private static Button createStyledButton(String text, boolean disabledInitially) {
        Button b = new Button(text);
        b.setMinWidth(DEFAULT_BUTTON_WIDTH);
        b.setStyle(BTN_STYLE);
        b.setOnMouseEntered(e -> b.setStyle(BTN_STYLE_HOVER));
        b.setOnMouseExited(e -> b.setStyle(BTN_STYLE));
        b.setDisable(disabledInitially);
        return b;
    }

    private static void addNameFilter(TextField nameField, Button okButton) {
        nameField.textProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) {
                okButton.setDisable(true);
                return;
            }
            String filtered = newV.replaceAll("[^A-Za-z]", "");
            if (filtered.length() > 3) filtered = filtered.substring(0, 3);
            if (!filtered.equals(newV)) {
                // evita loop: só atualiza se mudou
                nameField.setText(filtered);
            }
            okButton.setDisable(filtered.isEmpty());
        });
    }

    private static String sanitizeName(String raw) {
        String name = raw == null ? "" : raw.trim().toUpperCase();
        return name.isEmpty() ? "AAA" : name;
    }

    private static VBox buildForm(TextField nameField, Button okButton) {
        VBox layout = new VBox(DEFAULT_SPACING);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(DEFAULT_PADDING));
        layout.setBackground(DEFAULT_POPUPBACKGROUND);

        Label label = new Label("Digite seu nome:");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        layout.getChildren().addAll(label, nameField, okButton);
        return layout;
    }

    private static VBox buildForm(TextField nameField, Button okButton, Button backButton) {
        VBox layout = buildForm(nameField, okButton);
        layout.getChildren().add(backButton);
        return layout;
    }

    private static TranslateTransition slide(VBox node, double fromX, double toX, int millis) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(millis), node);
        tt.setFromX(fromX);
        tt.setToX(toX);
        tt.play();
        return tt;
    }
}