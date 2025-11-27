/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.constants;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class DefaultStyle {

    public static final Background DEFAULT_POPUPBACKGROUND = new Background(

            new BackgroundFill(

                Color.rgb(221, 221, 221),
                CornerRadii.EMPTY,
                Insets.EMPTY
            )
    );

    public static final Background DEFAULT_BACKGROUND = new Background(

            new BackgroundFill(

                Color.rgb(29, 25, 22),
                CornerRadii.EMPTY,
                Insets.EMPTY
    ));
}
