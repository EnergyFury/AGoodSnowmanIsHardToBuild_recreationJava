package pt.ipbeja.estig.po.snowman.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipbeja.estig.po.snowman.model.Board.BoardModel;
import pt.ipbeja.estig.po.snowman.model.enums.Direction;
import pt.ipbeja.estig.po.snowman.model.enums.PositionContent;
import pt.ipbeja.estig.po.snowman.model.enums.SnowballSize;
import pt.ipbeja.estig.po.snowman.model.interfaces.View;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static pt.ipbeja.estig.po.snowman.constants.Game.DEFAULT_COLS;
import static pt.ipbeja.estig.po.snowman.constants.Game.DEFAULT_ROWS;

/**
 * Test class for BoardModel.
 */

public class BoardModelTest {

    private BoardModel board;
    private Monster monster;

    @BeforeEach
    void setUp() {
        board = new BoardModel(DEFAULT_ROWS, DEFAULT_COLS);
        monster = board.getMonster();
        monster.setRow(5);
        monster.setCol(5);

        for (int r = 0; r < DEFAULT_ROWS; r++) {
            for (int c = 0; c < DEFAULT_COLS; c++) {
                PositionContent pc = board.getBoard().get(r).get(c);
                switch (pc) {
                    case DOORCLOSED_NORTH, DOORCLOSED_SOUTH, DOORCLOSED_EAST, DOORCLOSED_WEST,
                         DOOROPEN_NORTH, DOOROPEN_SOUTH, DOOROPEN_EAST, DOOROPEN_WEST -> {
                        board.getBoard().get(r).set(c, PositionContent.NO_SNOW);
                    }
                    default -> {}
                }
            }
        }

        View dummyView = new View() {
            @Override
            public void update() {}

            @Override
            public void showLevelCompleted() {}
        };
        board.setView(dummyView);
        board.getSnowballs().clear();
    }

    @Test
    public void testMonsterToTheLeft() {
        board.moveMonster(Direction.LEFT);
        assertEquals(5, board.getMonster().getRow(), "The monster should be in row 5");
        assertEquals(4, board.getMonster().getCol(), "The monster should be in column 4");
    }

    @Test
    public void testMonsterToTheRight() {
        board.moveMonster(Direction.RIGHT);
        assertEquals(5, board.getMonster().getRow(), "The monster should be in row 5");
        assertEquals(6, board.getMonster().getCol(), "The monster should be in column 4");
    }

    @Test
    public void testMonsterToTheUP() {
        board.moveMonster(Direction.UP);
        assertEquals(4, board.getMonster().getRow(), "The monster should be in row 5");
        assertEquals(5, board.getMonster().getCol(), "The monster should be in column 4");
    }

    @Test
    public void testMonsterToTheDown() {
        board.moveMonster(Direction.DOWN);
        assertEquals(6, board.getMonster().getRow(), "The monster should be in row 5");
        assertEquals(5, board.getMonster().getCol(), "The monster should be in column 4");
    }

    @Test
    public void testCreateAverageSnowball() {

        board.getSnowballs().add(new Snowball(5, 4, SnowballSize.SMALL));
        board.getBoard().get(5).set(3, PositionContent.SNOW);

        board.moveMonster(Direction.LEFT);

        Optional<Snowball> movedSnowball = board.getSnowballs().stream()
                .filter(s -> s.getRow() == 5 && s.getCol() == 3)
                .findFirst();

        assertTrue(movedSnowball.isPresent(), "Snowball should exist at [5,3]");

        assertEquals(SnowballSize.AVERAGE, movedSnowball.get().getSize(),
                "Snowball should be AVERAGE size");
    }

    @Test
    public void testCreateBigSnowball() {

        board.getSnowballs().add(new Snowball(5, 4, SnowballSize.SMALL));

        board.getBoard().get(5).set(3, PositionContent.SNOW);
        board.getBoard().get(5).set(2, PositionContent.SNOW);

        board.moveMonster(Direction.LEFT);
        board.moveMonster(Direction.LEFT);
        board.moveMonster(Direction.LEFT);

        Optional<Snowball> movedSnowball = board.getSnowballs().stream()
                .filter(s -> s.getRow() == 5 && s.getCol() == 2)
                .findFirst();

        assertTrue(movedSnowball.isPresent(), "Snowball should exist at [5,2]");
        assertEquals(SnowballSize.BIG, movedSnowball.get().getSize(),
                "Snowball should be BIG size");
    }

    @Test
    public void testMaintainBigSnowball() {

        board.getSnowballs().add(new Snowball(5, 6, SnowballSize.SMALL));
        board.getBoard().get(5).set(7, PositionContent.SNOW);
        board.getBoard().get(4).set(7, PositionContent.SNOW);
        board.getBoard().get(3).set(7, PositionContent.SNOW);
        board.getBoard().get(3).set(8, PositionContent.SNOW);

        board.moveMonster(Direction.RIGHT);

        board.moveMonster(Direction.DOWN);
        board.moveMonster(Direction.RIGHT);
        board.moveMonster(Direction.RIGHT);

        board.moveMonster(Direction.UP);
        board.moveMonster(Direction.UP);
        board.moveMonster(Direction.UP);

        Optional<Snowball> movedSnowball = board.getSnowballs().stream()
                .filter(s -> s.getRow() == 3 && s.getCol() == 7)
                .findFirst();

        assertTrue(movedSnowball.isPresent(),
                "Snowball should exist at (3,7)");

        assertEquals(SnowballSize.BIG, movedSnowball.get().getSize(),
                "Snowball should remain BIG size");
    }

    @Test
    public void testAverageBigSnowman() {

        monster.setRow(3);
        monster.setCol(3);

        board.getSnowballs().add(new Snowball(3, 6, SnowballSize.BIG));
        board.getSnowballs().add(new Snowball(3, 5, SnowballSize.AVERAGE));

        board.moveMonster(Direction.RIGHT);
        board.moveMonster(Direction.RIGHT);

        assertEquals(PositionContent.PARTIAL_SNOWMAN, board.getBoard().get(3).get(6),
                "Should have a PARTIAL_SNOWMAN at (3,6)");

        assertTrue(board.getSnowballs().isEmpty(),
                "All snowballs should be removed after combination");
    }

    @Test
    public void testCompleteSnowman() {

        monster.setRow(3);
        monster.setCol(3);

        board.getSnowballs().add(new Snowball(3, 6, SnowballSize.BIG));
        board.getSnowballs().add(new Snowball(3, 5, SnowballSize.AVERAGE));

        board.getSnowballs().add(new Snowball(4, 6, SnowballSize.SMALL));

        board.getBoard().get(DEFAULT_ROWS / 2).set(DEFAULT_COLS - 1, PositionContent.DOORCLOSED_EAST);

        board.moveMonster(Direction.RIGHT);
        board.moveMonster(Direction.RIGHT);
        board.moveMonster(Direction.DOWN);

        board.moveMonster(Direction.RIGHT);
        board.moveMonster(Direction.DOWN);

        board.moveMonster(Direction.RIGHT);
        board.moveMonster(Direction.UP);


        assertEquals(PositionContent.SNOWMAN, board.getBoard().get(3).get(6),
                "Should have a complete SNOWMAN at (3,6)");

        assertTrue(board.getSnowballs().isEmpty(),
                "Small snowball should be removed after completing snowman");

        board.moveMonster(Direction.RIGHT);

        assertEquals(PositionContent.DOOROPEN_EAST, board.getBoard().get(DEFAULT_ROWS / 2).get(DEFAULT_COLS - 1),
                "The east door should be open after completing the snowman.");
    }
}