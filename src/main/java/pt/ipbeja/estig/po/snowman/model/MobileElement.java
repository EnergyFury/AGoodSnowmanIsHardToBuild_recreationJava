/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.model;

import pt.ipbeja.estig.po.snowman.model.enums.Direction;

public abstract class MobileElement {

    protected int row;
    protected int col;
    protected Direction direction;

    public MobileElement(int row, int col) {

        this.row = row;
        this.col = col;
        this.direction = Direction.DOWN;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void move(int dRow, int dCol) {

        this.row += dRow;
        this.col += dCol;
    }
}
