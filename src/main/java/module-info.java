module pt.ipbeja.estig.po2.snowman {
    requires javafx.controls;
    requires javafx.graphics;
    requires java.desktop;
    exports pt.ipbeja.estig.po.snowman.gui;
    exports pt.ipbeja.estig.po.snowman.gui.SM;
    exports pt.ipbeja.estig.po.snowman.gui.SM.SMC;
    exports pt.ipbeja.estig.po.snowman.gui.GP;
    exports pt.ipbeja.estig.po.snowman.gui.NID;

    exports pt.ipbeja.estig.po.snowman.model;
    exports pt.ipbeja.estig.po.snowman.model.sound;
    exports pt.ipbeja.estig.po.snowman.model.interfaces;
}