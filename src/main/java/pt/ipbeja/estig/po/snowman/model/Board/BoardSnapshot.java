/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.model.Board;

import pt.ipbeja.estig.po.snowman.model.Monster;
import pt.ipbeja.estig.po.snowman.model.Snowball;
import pt.ipbeja.estig.po.snowman.model.enums.PositionContent;
import java.util.ArrayList;
import java.util.List;

public class BoardSnapshot {
    private final Monster monster;
    private final List<Snowball> snowballs;
    private final PositionContent[][] snow;

    public BoardSnapshot(Monster monster, List<Snowball> snowballs, PositionContent[][] snow) {
        this.monster = new Monster(monster.getRow(), monster.getCol());
        this.snowballs = new ArrayList<>();
        for (Snowball s : snowballs) {
            this.snowballs.add(new Snowball(s.getRow(), s.getCol(), s.getSize()));
        }

        this.snow = new PositionContent[snow.length][snow[0].length];
        for (int i = 0; i < snow.length; i++) {
            System.arraycopy(snow[i], 0, this.snow[i], 0, snow[i].length);
        }
    }

    public Monster getMonster() {
        return monster;
    }

    public List<Snowball> getSnowballs() {
        return snowballs;
    }

    public PositionContent[][] getSnow() {
        return snow;
    }
}