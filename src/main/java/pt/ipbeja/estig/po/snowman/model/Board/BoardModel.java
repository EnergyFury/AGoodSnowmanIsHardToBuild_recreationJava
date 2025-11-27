/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.model.Board;

import pt.ipbeja.estig.po.snowman.model.Monster;
import pt.ipbeja.estig.po.snowman.model.Score;
import pt.ipbeja.estig.po.snowman.model.Snowball;
import pt.ipbeja.estig.po.snowman.model.interfaces.View;
import pt.ipbeja.estig.po.snowman.model.enums.Direction;
import pt.ipbeja.estig.po.snowman.model.enums.PositionContent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.time.zone.ZoneOffsetTransitionRule.TimeDefinition.WALL;
import static javax.swing.text.SimpleAttributeSet.EMPTY;
import static pt.ipbeja.estig.po.snowman.constants.Game.*;
import static pt.ipbeja.estig.po.snowman.model.enums.Direction.*;
import static pt.ipbeja.estig.po.snowman.model.enums.PositionContent.SNOW;
import static pt.ipbeja.estig.po.snowman.model.enums.SnowballSize.*;

/**
 * BoardModel controls the board state and the monster/snowball logic.
 */

public class BoardModel {


    private final List<List<PositionContent>> board;
    private List<Snowball> snowballs;
    private PositionContent[][] snow;
    private Monster monster;
    private final List<String> moves;
    private final Stack<BoardSnapshot> undoStack;
    private final Stack<BoardSnapshot> redoStack;
    private View view;

    private final int rows;
    private final int cols;

    private boolean isSnowmanComplete = false;
    private boolean levelCompleted = false;

    private int currentLevel = 1;

    private List<Score> scores = new ArrayList<>();
    private String playerName = "AAA";

    public BoardModel(int rows, int cols) {

        this.rows = rows;
        this.cols = cols;
        this.board = new ArrayList<>();
        this.snowballs = new ArrayList<>();
        this.snow = new PositionContent[rows][cols];
        this.moves = new ArrayList<>();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        initBoard();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void initBoard() {

        isSnowmanComplete = false;

        board.clear();
        snowballs.clear();
        moves.clear();
        undoStack.clear();
        redoStack.clear();

        for (int i = 0; i < rows; i++) {
            List<PositionContent> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(PositionContent.NO_SNOW);
            }
            board.add(row);
        }

        switch (currentLevel) {
            case 1 -> initLevel1();
            case 2 -> initLevel2();
            case 3 -> initLevel3();
            case 4 -> initLevel4();
            case 5 -> initLevel5();
            default -> initLevel1();
        }
    }

    private void initLevel1() {

        addBorderBlocks(RIGHT);

        monster = new Monster((DEFAULT_ROWS / 2), (DEFAULT_COLS / 2));

        snowballs.add(new Snowball(2, 6, SMALL));
        snowballs.add(new Snowball(2, 4, AVERAGE));
        snowballs.add(new Snowball(2, 2, BIG));
    }

    private void initLevel2() {

        addBorderBlocks(UP);

        monster = new Monster((DEFAULT_ROWS / 2), (DEFAULT_COLS / 2));

        snowballs.add(new Snowball(6, 6, SMALL));
        snowballs.add(new Snowball(6, 4, AVERAGE));
        snowballs.add(new Snowball(6, 2, AVERAGE));

        board.get(4).set(4, SNOW);
    }

    private void initLevel3() {

        addBorderBlocks(LEFT);

        monster = new Monster((DEFAULT_ROWS / 2), (DEFAULT_COLS / 2));

        snowballs.add(new Snowball(3, 5, SMALL));
        snowballs.add(new Snowball(5, 6, SMALL));
        snowballs.add(new Snowball(2, 3, AVERAGE));

        board.get(3).set(2, SNOW);
        board.get(6).set(5, SNOW);
    }

    private void initLevel4() {
        addBorderBlocks(DOWN);

        monster = new Monster((DEFAULT_ROWS / 2), (DEFAULT_COLS / 2));

        snowballs.add(new Snowball(6, 2, SMALL));
        snowballs.add(new Snowball(2, 5, SMALL));
        snowballs.add(new Snowball(5, 6, SMALL));

        board.get(2).set(3, SNOW);
        board.get(4).set(3, SNOW);
        board.get(6).set(6, SNOW);
    }

    private void initLevel5() {

        addBorderBlocks(LEFT);

        monster = new Monster((DEFAULT_ROWS / 2), (DEFAULT_COLS / 2));
    }

    public void nextLevel() {

        this.currentLevel++;
        this.initBoard();
        this.view.update();
    }

    public void moveMonster(Direction dir) {

        monster.setDirection(dir);

        int dRow = 0, dCol = 0;

        switch (dir) {
            case UP -> dRow = -1;
            case DOWN -> dRow = 1;
            case LEFT -> dCol = -1;
            case RIGHT -> dCol = 1;
        }

        int nextRow = monster.getRow() + dRow;
        int nextCol = monster.getCol() + dCol;

        if (!isInsideBoard(nextRow, nextCol)) return;

        Optional<Snowball> ball = findSnowball(nextRow, nextCol);

        if (ball.isPresent()) {
            int ballNextRow = nextRow + dRow;
            int ballNextCol = nextCol + dCol;

            if (!isInsideBoard(ballNextRow, ballNextCol)) return;

            // Verifica se a próxima posição é um boneco parcial
            if (ball.get().getSize() == SMALL &&
                    board.get(ballNextRow).get(ballNextCol)
                            == PositionContent.PARTIAL_SNOWMAN) {
                saveState();
                completeSnowman(ball.get(), ballNextRow, ballNextCol);
                return;
            }

            if (isBlocked(ballNextRow, ballNextCol)) return;

            Optional<Snowball> targetBall = findSnowball(ballNextRow, ballNextCol);
            if (targetBall.isPresent()) {
                if (canCombineSnowballs(ball.get(), targetBall.get())) {
                    saveState();
                    combineSnowballs(ball.get(), targetBall.get());
                    return;
                }
                return;
            }

            saveState();
            ball.get().move(dRow, dCol);

            PositionContent under = board.get(ballNextRow).get(ballNextCol);
            if (under == SNOW) {
                growSnowball(ball.get());
                board.get(ballNextRow).set(ballNextCol, PositionContent.NO_SNOW);
            }
        } else {
            PositionContent nextPosition = board.get(nextRow).get(nextCol);

            if (nextPosition == PositionContent.DOOROPEN_NORTH) {
                saveState();
                monster.move(dRow, dCol);
                levelCompleted = true;
                saveScore();
                view.showLevelCompleted();
                return;
            }
            if (nextPosition == PositionContent.DOOROPEN_SOUTH) {
                saveState();
                monster.move(dRow, dCol);
                levelCompleted = true;
                saveScore();
                view.showLevelCompleted();
                return;
            }
            if (nextPosition == PositionContent.DOOROPEN_EAST) {
                saveState();
                monster.move(dRow, dCol);
                levelCompleted = true;
                saveScore();
                view.showLevelCompleted();
                return;
            }
            if (nextPosition == PositionContent.DOOROPEN_WEST) {
                saveState();
                monster.move(dRow, dCol);
                levelCompleted = true;
                saveScore();
                view.showLevelCompleted();
                return;
            }

            if (isBlocked(nextRow, nextCol)) return;
            saveState();
            monster.move(dRow, dCol);
        }

        moves.add(formatMove(monster.getRow(), monster.getCol()));
        view.update();
    }

    private boolean canCombineSnowballs(Snowball pushedBall, Snowball targetBall) {
        return (pushedBall.getSize() == BIG && targetBall.getSize() == AVERAGE) ||
                (pushedBall.getSize() == AVERAGE && targetBall.getSize() == BIG);
    }

    public void combineSnowballs(Snowball pushedBall, Snowball targetBall) {

        // Determina qual é a bola grande e a média
        Snowball bigBall = pushedBall.getSize() == BIG
                ? pushedBall : targetBall;
        Snowball averageBall = pushedBall.getSize() == AVERAGE
                ? pushedBall : targetBall;

        snowballs.remove(pushedBall);
        snowballs.remove(targetBall);

        board.get(pushedBall.getRow()).set(pushedBall.getCol(), PositionContent.NO_SNOW);
        board.get(targetBall.getRow()).set(targetBall.getCol(), PositionContent.NO_SNOW);

        if (pushedBall.getSize() == BIG) {
            board.get(averageBall.getRow()).set(averageBall.getCol(),
                    PositionContent.PARTIAL_SNOWMAN);
        }
        else {
            board.get(bigBall.getRow()).set(bigBall.getCol(),
                    PositionContent.PARTIAL_SNOWMAN);
        }

        view.update();
    }

    private void growSnowball(Snowball snowball) {
        switch (snowball.getSize()) {
            case SMALL -> snowball.setSize(AVERAGE);
            case AVERAGE -> snowball.setSize(BIG);
            case BIG -> {}
        }
    }

    private Optional<Snowball> findSnowball(int row, int col) {
        return snowballs.stream()
                .filter(s -> s.getRow() == row && s.getCol() == col)
                .findFirst();
    }

    private boolean isBlocked(int row, int col) {
        PositionContent content = board.get(row).get(col);
        return content == PositionContent.BLOCK ||
                content == PositionContent.WALL_CORNER_SE ||
                content == PositionContent.WALL_CORNER_NW ||
                content == PositionContent.WALL_CORNER_NE ||
                content == PositionContent.WALL_CORNER_SW ||
                content == PositionContent.WALL_NORTH ||
                content == PositionContent.WALL_EAST ||
                content == PositionContent.WALL_SOUTH ||
                content == PositionContent.WALL_WEST ||
                content == PositionContent.PARTIAL_SNOWMAN ||
                content == PositionContent.SNOWMAN ||
                content == PositionContent.DOORCLOSED_NORTH ||
                content == PositionContent.DOORCLOSED_SOUTH ||
                content == PositionContent.DOORCLOSED_EAST ||
                content == PositionContent.DOORCLOSED_WEST;
    }

    public void completeSnowman(Snowball smallBall, int targetRow, int targetCol) {

        snowballs.remove(smallBall);

        board.get(smallBall.getRow()).set(smallBall.getCol(), PositionContent.NO_SNOW);

        // Transforma o boneco parcial em completo
        board.get(targetRow).set(targetCol, PositionContent.SNOWMAN);
        isSnowmanComplete = true;

        // Abre a porta
        for (List<PositionContent> positionContents : board) {
            for (int j = 0; j < positionContents.size(); j++) {
                if (positionContents.get(j) == PositionContent.DOORCLOSED_EAST) {
                    positionContents.set(j, PositionContent.DOOROPEN_EAST);
                    view.update();
                    return;
                }
                if (positionContents.get(j) == PositionContent.DOORCLOSED_WEST) {
                    positionContents.set(j, PositionContent.DOOROPEN_WEST);
                    view.update();
                    return;
                }
                if (positionContents.get(j) == PositionContent.DOORCLOSED_NORTH) {
                    positionContents.set(j, PositionContent.DOOROPEN_NORTH);
                    view.update();
                    return;
                }
                if (positionContents.get(j) == PositionContent.DOORCLOSED_SOUTH) {
                    positionContents.set(j, PositionContent.DOOROPEN_SOUTH);
                    view.update();
                    return;
                }
            }
        }
    }

    private boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private void saveState() {
        undoStack.push(new BoardSnapshot(copyMonster(), copySnowballs(), copySnow()));
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new BoardSnapshot(monster, snowballs, copySnow()));
            BoardSnapshot previous = undoStack.pop();
            this.monster = previous.getMonster();
            this.snowballs = previous.getSnowballs();
            this.snow = previous.getSnow();
            updateBoardFromSnapshot(previous.getSnow());
            view.update();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new BoardSnapshot(monster, snowballs, copySnow()));
            BoardSnapshot next = redoStack.pop();
            this.monster = next.getMonster();
            this.snowballs = next.getSnowballs();
            this.snow = next.getSnow();
            updateBoardFromSnapshot(next.getSnow());
            view.update();
        }
    }

    private Monster copyMonster() {
        return new Monster(monster.getRow(), monster.getCol());
    }

    private List<Snowball> copySnowballs() {
        List<Snowball> copy = new ArrayList<>();
        for (Snowball s : snowballs) {
            copy.add(new Snowball(s.getRow(), s.getCol(), s.getSize()));
        }
        return copy;
    }

    private PositionContent[][] copySnow() {
        PositionContent[][] copy = new PositionContent[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = board.get(i).get(j);
            }
        }
        return copy;
    }


    private String formatMove(int row, int col) {
        return "(" + (row + 1) + ", " + (char) ('A' + col) + ")";
    }

    private void saveScore() {
        try {
            List<String> lines = new ArrayList<>();

            lines.add("=== ESTADO DO JOGO ===");

            lines.add("Nível: " + currentLevel);
            lines.add("Jogador: " + playerName);
            lines.add("");

            lines.add("Monstro:");
            lines.add("  Posição: (" + monster.getRow() + ", " + monster.getCol() + ")");
            lines.add("  Direção: " + monster.getDirection());
            lines.add("");

            lines.add("Bolas de Neve: " + snowballs.size());
            for (Snowball sb : snowballs) {
                lines.add("  - (" + sb.getRow() + ", " + sb.getCol() + "), Tamanho: " + sb.getSize());
            }
            lines.add("");

            lines.add("Tabuleiro:");
            for (List<PositionContent> row : board) {
                StringBuilder sb = new StringBuilder("  ");
                for (PositionContent tiles : row) {
                    sb.append(tiles.name());
                }
                lines.add(sb.toString());
            }
            lines.add("");

            lines.add("Movimentos:");
            int count = 1;
            for (String move : moves) {
                lines.add("  " + count + ". " + move);
                count++;
            }
            lines.add("Total de movimentos: " + moves.size());

            Path path = Path.of(FILENAME);
            Files.write(path, lines);

            System.out.println("Jogo guardado com sucesso em " + FILENAME);
        } catch (Exception e) {
            System.err.println("Erro ao guardar o jogo: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void updateBoardFromSnapshot(PositionContent[][] snow) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board.get(i).set(j, snow[i][j]);
            }
        }
    }

    private void addBorderBlocks(Direction doorWall) {

        for (int col = 0; col < cols; col++) {

            if (col == 0) {
                board.get(0).set(col, PositionContent.WALL_CORNER_NW);
            } else if (col == cols - 1) {
                board.get(0).set(col, PositionContent.WALL_CORNER_NE);
            } else if (doorWall == Direction.UP && col == cols / 2) {
                board.get(0).set(col, PositionContent.DOORCLOSED_NORTH);
            } else {
                board.get(0).set(col, PositionContent.WALL_NORTH);
            }

            if (col == 0) {
                board.get(rows - 1).set(col, PositionContent.WALL_CORNER_SW);
            } else if (col == cols - 1) {
                board.get(rows - 1).set(col, PositionContent.WALL_CORNER_SE);
            } else if (doorWall == Direction.DOWN && col == cols / 2) {
                board.get(rows - 1).set(col, PositionContent.DOORCLOSED_SOUTH);
            } else {
                board.get(rows - 1).set(col, PositionContent.WALL_SOUTH);
            }
        }

        for (int row = 1; row < rows - 1; row++) {
            if (doorWall == Direction.LEFT && row == rows / 2) {
                board.get(row).set(0, PositionContent.DOORCLOSED_WEST);
            } else {
                board.get(row).set(0, PositionContent.WALL_WEST);
            }

            if (doorWall == Direction.RIGHT && row == rows / 2) {
                board.get(row).set(cols - 1, PositionContent.DOORCLOSED_EAST);
            } else {
                board.get(row).set(cols - 1, PositionContent.WALL_EAST);
            }
        }
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<List<PositionContent>> getBoard() {
        return board;
    }

    public List<Snowball> getSnowballs() {
        return snowballs;
    }

    public Monster getMonster() {
        return monster;
    }
}