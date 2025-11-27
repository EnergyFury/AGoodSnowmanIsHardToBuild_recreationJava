/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Score implements Comparable<Score> {

    private final String playerName;
    private final int moves;
    private final int level;
    private final LocalDateTime completionTime;

    public Score(String playerName, int moves, int level, LocalDateTime completionTime) {
        this.playerName = playerName;
        this.moves = moves;
        this.level = level;
        this.completionTime = completionTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getMoves() {
        return moves;
    }

    public int getLevel() {
        return level;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    @Override
    public int compareTo(Score other) {
        return Integer.compare(this.moves, other.moves);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return playerName + " - Level " + level + " - Moves: " + moves +
                " - Time: " + completionTime.format(formatter);
    }
}

