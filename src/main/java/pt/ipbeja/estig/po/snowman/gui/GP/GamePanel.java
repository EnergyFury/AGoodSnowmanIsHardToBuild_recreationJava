/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.gui.GP;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt.ipbeja.estig.po.snowman.gui.GP.GSM.GameStateManager;
import pt.ipbeja.estig.po.snowman.model.Monster;
import pt.ipbeja.estig.po.snowman.model.Snowball;
import pt.ipbeja.estig.po.snowman.model.interfaces.View;
import pt.ipbeja.estig.po.snowman.model.Board.BoardModel;
import pt.ipbeja.estig.po.snowman.model.enums.Direction;
import pt.ipbeja.estig.po.snowman.model.enums.PositionContent;

import java.util.Objects;
import java.util.Optional;

import static pt.ipbeja.estig.po.snowman.constants.Game.*;
import static pt.ipbeja.estig.po.snowman.constants.Game.WALL_CORNER_SE;

public class GamePanel extends BorderPane implements View {

    private final BoardModel boardModel;
    private final GameStateManager gameStateManager;
    private final Canvas canvas;
    private final VBox coorPanel;
    private final TextArea coordinates;

    private int previousRow;
    private int previousCol;

    public GamePanel(GameStateManager gameStateManager) {

        this.gameStateManager = gameStateManager;
        this.boardModel = new BoardModel(DEFAULT_ROWS, DEFAULT_COLS);
        this.boardModel.setView(this);

        this.canvas = new Canvas(DEFAULT_COLS * CELLSIZE, DEFAULT_ROWS * CELLSIZE);
        this.setCenter(canvas);

        this.coorPanel = new VBox(10);
        this.coorPanel.setPrefWidth(150);
        this.coorPanel.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5;");

        Label title = new Label("Movimentos:");
        title.setStyle("-fx-font-weight: bold");

        this.coordinates = new TextArea();
        this.coordinates.setEditable(false);
        this.coordinates.setPrefRowCount(15);
        this.coordinates.setStyle("-fx-font-family: monospace");

        this.setFocusTraversable(true);
        this.setOnKeyPressed(this::handleKeyPress);

        coorPanel.getChildren().addAll(title, coordinates);
        this.setRight(coorPanel);

        draw();
    }

    public String playerName;

    public void setPlayerName(String name) {
        this.playerName = name;
        this.boardModel.setPlayerName(name);
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case W -> boardModel.moveMonster(Direction.UP);
            case A -> boardModel.moveMonster(Direction.LEFT);
            case S -> boardModel.moveMonster(Direction.DOWN);
            case D -> boardModel.moveMonster(Direction.RIGHT);
            case Z -> boardModel.undo();
            case X -> boardModel.redo();
            case ESCAPE -> gameStateManager.togglePause();
        }

        Monster newMonster = boardModel.getMonster();
        logMovement(previousRow, previousCol, newMonster.getRow(), newMonster.getCol());

        draw();
    }

    private void logMovement(int fromRow, int fromCol, int toRow, int toCol) {
        String fromColLetter = String.valueOf((char) ('A' + fromCol));
        String toColLetter = String.valueOf((char) ('A' + toCol));

        String move = String.format("(%d, %s) → (%d, %s)", fromRow, fromColLetter, toRow, toColLetter);
        coordinates.appendText(move + "\n");
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw grid
        var board = boardModel.getBoard();
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                drawCell(gc, i, j, board.get(i).get(j));
            }
        }

        for (Snowball s : boardModel.getSnowballs()) {
            if (board.get(s.getRow()).get(s.getCol()) != PositionContent.PARTIAL_SNOWMAN &&
                    board.get(s.getRow()).get(s.getCol()) != PositionContent.SNOWMAN) {
                drawSnowball(gc, s);
            }
        }

        // Draw Monster
        drawMonster(gc, boardModel.getMonster());
    }


    private void drawMonster(GraphicsContext gc, Monster monster) {
        try {
            String spritePath = switch (monster.getDirection()) {
                case UP -> DARK_PLAYER_UP_IMAGE;
                case DOWN -> DARK_PLAYER_DOWN_IMAGE;
                case LEFT -> DARK_PLAYER_LEFT_IMAGE;
                case RIGHT -> DARK_PLAYER_RIGHT_IMAGE;
            };

            Image playerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(spritePath)));
            gc.drawImage(playerImage,
                    monster.getCol() * CELLSIZE,
                    monster.getRow() * CELLSIZE,
                    CELLSIZE,
                    CELLSIZE);
        } catch (Exception e) {
            gc.setFill(Color.BLACK);
            gc.fillOval(monster.getCol() * CELLSIZE,
                    monster.getRow() * CELLSIZE,
                    CELLSIZE,
                    CELLSIZE);
        }
    }

    private void drawCell(GraphicsContext gc, int row, int col, PositionContent content) {
        double x = col * CELLSIZE;
        double y = row * CELLSIZE;

        if (content != PositionContent.BLOCK) {
            gc.setFill(Color.rgb(98,170,74));
            gc.fillRect(x, y, CELLSIZE, CELLSIZE);

            // Se for SNOW, desenha a neve por cima da relva
            if (content == PositionContent.SNOW) {
                try {
                    Image snowImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    SNOWIMAGE)));
                    gc.drawImage(snowImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.WHITE);
                    gc.setGlobalAlpha(0.9);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                    gc.setGlobalAlpha(1.0);
                }
                return;
            }
        }

        switch (content) {
            case WALL_NORTH -> {
                try {
                    Image partialSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    WALL_NORTH)));
                    gc.drawImage(partialSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                    System.err.println("Erro a carregar imagem da parede: " + e.getMessage());
                }
            }
            case WALL_SOUTH -> {
                try {
                    Image partialSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    WALL_SOUTH)));
                    gc.drawImage(partialSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                    System.err.println("Erro a carregar imagem da parede: " + e.getMessage());
                }
            }
            case WALL_EAST -> {
                try {
                    Image partialSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    WALL_EAST)));
                    gc.drawImage(partialSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                    System.err.println("Erro a carregar imagem da parede: " + e.getMessage());
                }
            }
            case WALL_WEST -> {
                try {
                    Image partialSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    WALL_WEST)));
                    gc.drawImage(partialSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                    System.err.println("Erro a carregar imagem da parede: " + e.getMessage());
                }
            }
            case WALL_CORNER_NW -> {
                try {
                    Image partialSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    WALL_CORNER_NW)));
                    gc.drawImage(partialSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                    System.err.println("Erro a carregar imagem da parede: " + e.getMessage());
                }
            }
            case WALL_CORNER_NE -> {
                try {
                    Image partialSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    WALL_CORNER_NE)));
                    gc.drawImage(partialSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                    System.err.println("Erro a carregar imagem da parede: " + e.getMessage());
                }
            }
            case WALL_CORNER_SW -> {
                try {
                    Image partialSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    WALL_CORNER_SW)));
                    gc.drawImage(partialSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                    System.err.println("Erro a carregar imagem da parede: " + e.getMessage());
                }
            }
            case WALL_CORNER_SE -> {
                try {
                    Image partialSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    WALL_CORNER_SE)));
                    gc.drawImage(partialSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                    System.err.println("Erro a carregar imagem da parede: " + e.getMessage());
                }
            }
            case PARTIAL_SNOWMAN -> {
                try {
                    Image partialSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    SNOWMANBASEMID_IMAGE)));
                    gc.drawImage(partialSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.LIGHTBLUE);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
            case SNOWMAN -> {
                try {
                    Image completeSnowmanImage =
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                                    SNOWMANCOMPLETE_IMAGE)));
                    gc.drawImage(completeSnowmanImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GOLD);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
            case DOORCLOSED_NORTH -> {
                try {
                    Image doorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            DOORCLOSED_NORTH_IMAGE)));
                    gc.drawImage(doorImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.RED);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
            case DOORCLOSED_WEST -> {
                try {
                    Image doorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            DOORCLOSED_WEST_IMAGE)));
                    gc.drawImage(doorImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.RED);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
            case DOORCLOSED_SOUTH -> {
                try {
                    Image doorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            DOORCLOSED_SOUTH_IMAGE)));
                    gc.drawImage(doorImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.RED);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
            case DOORCLOSED_EAST -> {
                try {
                    Image doorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            DOORCLOSED_EAST_IMAGE)));
                    gc.drawImage(doorImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.RED);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
            case DOOROPEN_WEST -> {
                try {
                    Image doorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            DOOROPEN_WEST_IMAGE)));
                    gc.drawImage(doorImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
            case DOOROPEN_NORTH -> {
                try {
                    Image doorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            DOOROPEN_NORTH_IMAGE)));
                    gc.drawImage(doorImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
            case DOOROPEN_EAST -> {
                try {
                    Image doorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            DOOROPEN_EAST_IMAGE)));
                    gc.drawImage(doorImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
            case DOOROPEN_SOUTH -> {
                try {
                    Image doorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            DOOROPEN_SOUTH_IMAGE)));
                    gc.drawImage(doorImage, x, y, CELLSIZE, CELLSIZE);
                } catch (Exception e) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(x, y, CELLSIZE, CELLSIZE);
                }
            }
        }
    }

    private void drawSnowball(GraphicsContext gc, Snowball s) {
        double x = s.getCol() * CELLSIZE;
        double y = s.getRow() * CELLSIZE;

        double snowmanSize = switch (s.getSize()) {
            case SMALL -> CELLSIZE - 40;
            case AVERAGE -> CELLSIZE - 30;
            case BIG -> CELLSIZE - 25;
        };

        double offsetX = (CELLSIZE - snowmanSize) / 2;
        double offsetY = CELLSIZE - snowmanSize;

        try {
            Image snowballImage =
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                            SNOWBALL_IMAGE)));
            gc.drawImage(snowballImage, x + offsetX, y + offsetY, snowmanSize, snowmanSize);
        } catch (Exception e) {
            Color snowballColor = switch (s.getSize()) {
                case SMALL -> Color.LIGHTGRAY;
                case AVERAGE -> Color.WHITE;
                case BIG -> Color.SNOW;
            };
            gc.setFill(snowballColor);
            gc.fillOval(x + offsetX, y + offsetY, snowmanSize, snowmanSize);
        }
    }

    @Override
    public void update() {
        draw();
    }

    @Override
    public void showLevelCompleted() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Nível Completo");

        alert.getButtonTypes().clear();

        // Cria botões personalizados
        ButtonType mainMenuButton = new ButtonType("Voltar ao Menu Principal");
        ButtonType restartButton = new ButtonType("Recomeçar Nível");
        ButtonType nextButton = new ButtonType("Próximo Nível");
        alert.getButtonTypes().addAll(mainMenuButton, restartButton, nextButton);

        // Personaliza o conteúdo
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);

        Label trophyLabel = new Label("🏆");
        trophyLabel.setStyle("-fx-font-size: 48px;");

        // Textos
        Label congratsLabel = new Label("Parabéns!");
        congratsLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        Label completedLabel = new Label("Você completou o boneco de neve!");
        completedLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495E;");

        content.getChildren().addAll(trophyLabel, congratsLabel, completedLabel);

        // Estiliza o diálogo
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.setStyle("""
            -fx-background-color: white;
            -fx-padding: 20;
            -fx-background-radius: 10;
            """);

        // Estiliza os botões

        Node mainMenuButtonNode = dialogPane.lookupButton(mainMenuButton);
        mainMenuButtonNode.setStyle("""
            -fx-background-color: #3498DB;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """);

        mainMenuButtonNode.setOnMouseEntered(e ->
                mainMenuButtonNode.setStyle("""
            -fx-background-color: #2980B9;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """)
        );
        mainMenuButtonNode.setOnMouseExited(e ->
                mainMenuButtonNode.setStyle("""
            -fx-background-color: #3498DB;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """)
        );

        Node restartButtonNode = dialogPane.lookupButton(restartButton);
        restartButtonNode.setStyle("""
            -fx-background-color: #3498DB;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """);

        restartButtonNode.setOnMouseEntered(e ->
                restartButtonNode.setStyle("""
            -fx-background-color: #2980B9;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """)
        );
        restartButtonNode.setOnMouseExited(e ->
                restartButtonNode.setStyle("""
            -fx-background-color: #3498DB;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """)
        );

        Node nextButtonNode = dialogPane.lookupButton(nextButton);
        nextButtonNode.setStyle("""
            -fx-background-color: #3498DB;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """);

        nextButtonNode.setOnMouseEntered(e ->
                nextButtonNode.setStyle("""
            -fx-background-color: #2980B9;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """)
        );
        nextButtonNode.setOnMouseExited(e ->
                nextButtonNode.setStyle("""
            -fx-background-color: #3498DB;
            -fx-text-fill: white;
            -fx-background-radius: 5;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            """)
        );

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == mainMenuButton) {
                gameStateManager.returnToMainMenu();
            }
            else if (buttonType == restartButton) {
                restartLevel();
            } else if (buttonType == nextButton) {
                nextLevel();
            }
        });
    }

    private void restartLevel() {
        this.boardModel.initBoard();
        draw();
    }

    private void nextLevel() {
        this.boardModel.nextLevel();
        draw();
    }
}