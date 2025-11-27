/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game {

    private Game() {
        throw new AssertionError("Esta classe não deve ser instanciada");
    }

    public static final String GAME_TITLE = "A Good Snowman Is Hard To Build";
    public static final String PAUSEMENU_TITLE = "Pause Menu";
    public static final String GAME_TITLE_SUFFIX = " - PO2";
    public static final String COMPLETE_GAME_TITLE = GAME_TITLE + GAME_TITLE_SUFFIX;

    static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final String FILENAME = "snowman" + LocalDateTime.now().format(FORMATTER) + ".txt";
    public static final String CONFIG_FILE = "config.properties";

    // Resource Paths
    public static final String SPRITES_PATH = "/sprites/";


    // Fonts
    public static final String FONT_PATH = "/fonts/";
    public static final String FONT_01 = FONT_PATH + "Signika-Bold.ttf";
    public static final double FONT_01_SIZE = 18;

    //Music
    public static double VOLUME_MUSIC = 0.5;
    public static final String MUSIC_PATH = "/music/";
    public static final String BACKGROUND_MUSIC_PATH = MUSIC_PATH + "background.wav";

    //Tiles
    public static final String WALL_PATH = "/sprites/tiles/wall/";
    public static final String WALL_NORTH = WALL_PATH + "wall_north.png";
    public static final String WALL_SOUTH = WALL_PATH + "wall_south.png";
    public static final String WALL_EAST = WALL_PATH + "wall_east.png";
    public static final String WALL_WEST = WALL_PATH + "wall_west.png";

    public static final String WALL_CORNER_PATH = "/sprites/tiles/wall/corners/";
    public static final String WALL_CORNER_NW = WALL_CORNER_PATH + "wall_corner_nw.png";
    public static final String WALL_CORNER_NE = WALL_CORNER_PATH + "wall_corner_ne.png";
    public static final String WALL_CORNER_SE = WALL_CORNER_PATH + "wall_corner_se.png";
    public static final String WALL_CORNER_SW = WALL_CORNER_PATH + "wall_corner_sw.png";

    // Player
    public static final String DARK_PLAYERSPRITES_PATH = "/sprites/playerdark/";
    public static final String DARK_PLAYER_UP_IMAGE = DARK_PLAYERSPRITES_PATH + "dark_player_up.png";
    public static final String DARK_PLAYER_DOWN_IMAGE = DARK_PLAYERSPRITES_PATH + "dark_player_down.png";
    public static final String DARK_PLAYER_LEFT_IMAGE = DARK_PLAYERSPRITES_PATH + "dark_player_left.png";
    public static final String DARK_PLAYER_RIGHT_IMAGE = DARK_PLAYERSPRITES_PATH + "dark_player_right.png";

    public static final String PLAYERSPRITES_PATH = "/sprites/player/";
    public static final String PLAYER_UP_IMAGE = PLAYERSPRITES_PATH + "player_up.png";
    public static final String PLAYER_DOWN_IMAGE = PLAYERSPRITES_PATH + "player_down.png";
    public static final String PLAYER_LEFT_IMAGE = PLAYERSPRITES_PATH + "player_left.png";
    public static final String PLAYER_RIGHT_IMAGE = PLAYERSPRITES_PATH + "player_right.png";

    //Other
    public static final String BACKGROUNDS_PATH = "/background/";
    public static final String MENUIMAGE_LOGO = BACKGROUNDS_PATH + "logo.png";
    public static final String SNOWIMAGE = SPRITES_PATH + "snow.png";
    public static final String SNOWBALL_IMAGE = SPRITES_PATH + "snowball.png";
    public static final String SNOWMANBASEMID_IMAGE = SPRITES_PATH + "snowman_base_mid.png";
    public static final String SNOWMANCOMPLETE_IMAGE = SPRITES_PATH + "snowman_complete.png";

    public static final String DOORCLOSED_PATH = "/sprites/tiles/door/doorclosed/";
    public static final String DOORCLOSED_NORTH_IMAGE = DOORCLOSED_PATH + "door_north_closed.png";
    public static final String DOORCLOSED_SOUTH_IMAGE = DOORCLOSED_PATH + "door_south_closed.png";
    public static final String DOORCLOSED_EAST_IMAGE = DOORCLOSED_PATH + "door_east_closed.png";
    public static final String DOORCLOSED_WEST_IMAGE = DOORCLOSED_PATH + "door_west_closed.png";

    public static final String DOOROPEN_PATH = "/sprites/tiles/door/dooropen/";
    public static final String DOOROPEN_NORTH_IMAGE = DOOROPEN_PATH + "door_north_open.png";
    public static final String DOOROPEN_SOUTH_IMAGE = DOOROPEN_PATH + "door_south_open.png";
    public static final String DOOROPEN_EAST_IMAGE = DOOROPEN_PATH + "door_east_open.png";
    public static final String DOOROPEN_WEST_IMAGE = DOOROPEN_PATH + "door_west_open.png";

    public static final double DEFAULT_BUTTON_WIDTH = 200;
    public static final double DEFAULT_SPACING = 20;
    public static final double DEFAULT_PADDING = 10;

    public static final double PAUSE_MENU_WIDTH = 400;
    public static final double PAUSE_MENU_HEIGHT = 500;


    public static final int DEFAULT_ROWS = 9;
    public static final int DEFAULT_COLS = 9;
    public static final double CELLSIZE = 60;
}
