/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.model;

import pt.ipbeja.estig.po.snowman.model.enums.SnowballSize;

public class Snowball extends MobileElement {

    private SnowballSize size;

    public Snowball(int row, int col, SnowballSize size) {

        super(row, col);
        this.size = size;
    }

    public SnowballSize getSize() {

        return this.size;
    }

    public void setSize(SnowballSize size) {

        this.size = size;
    }
}

