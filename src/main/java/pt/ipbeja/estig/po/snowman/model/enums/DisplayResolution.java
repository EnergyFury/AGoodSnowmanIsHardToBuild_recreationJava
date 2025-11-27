/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.model.enums;

public enum DisplayResolution {
    R_960x540(960, 540, "960x540 (Default)"),
    R_1280x720(1280, 720, "1280x720"),
    R_1600x900(1600, 900, "1600x900"),
    R_1920x1080(1920, 1080, "1920x1080");

    private final int width;
    private final int height;
    private final String label;

    DisplayResolution(int width, int height, String label) {
        this.width = width;
        this.height = height;
        this.label = label;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return label;
    }
}
