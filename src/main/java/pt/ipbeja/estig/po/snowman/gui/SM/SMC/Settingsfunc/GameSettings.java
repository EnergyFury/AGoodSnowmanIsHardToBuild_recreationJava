/***
 * Nomes:         Numero:
 * José Cabaço    26906
 * Juliano Santos 27133
 * */

package pt.ipbeja.estig.po.snowman.gui.SM.SMC.Settingsfunc;

import pt.ipbeja.estig.po.snowman.model.enums.DisplayResolution;

import static pt.ipbeja.estig.po.snowman.constants.Game.CONFIG_FILE;
import static pt.ipbeja.estig.po.snowman.constants.Game.VOLUME_MUSIC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class GameSettings {

    private static final GameSettings instance = new GameSettings();
    private final Properties properties;
    private boolean borderless;
    public static DisplayResolution currentResolution;
    public static boolean isFullscreen;
    private int selectedScreenIndex;

    private GameSettings() {

        this.properties = new Properties();

        loadSettings();
    }

    public static GameSettings getInstance() {
        return instance;
    }
    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setFullscreen(boolean fullscreen) {

        isFullscreen = fullscreen;

        saveSettings();
    }

    public DisplayResolution getResolution() {
        return currentResolution;
    }

    public void setResolution(DisplayResolution resolution) {

        currentResolution = resolution;

        saveSettings();
    }

    public double getMusicVolume() {
        return VOLUME_MUSIC;
    }

    public void setMusicVolume(double musicVolume) {

        VOLUME_MUSIC = musicVolume;

        saveSettings();
    }

    public int getSelectedScreenIndex() {
        return selectedScreenIndex;
    }

    public void setSelectedScreenIndex(int selectedScreenIndex) {

        this.selectedScreenIndex = selectedScreenIndex;

        saveSettings();
    }

    private void loadSettings() {

        File configFile = new File(CONFIG_FILE);

        if (!configFile.exists()) {

            setDefaultSettings();
            saveSettings();

            return;
        }

        try (FileInputStream input = new FileInputStream(configFile)) {

            properties.load(input);

            String volumeStr = properties.getProperty("volume");

            if (volumeStr != null) {

                double loadedVolume = Double.parseDouble(volumeStr);
                VOLUME_MUSIC = loadedVolume;
            }

            loadOtherSettings();

        } catch (IOException | NumberFormatException e) {

            System.err.println("Erro ao carregar configurações: " + e.getMessage());
            setDefaultSettings();
        }
    }

    private void loadOtherSettings() {

        String resolutionStr = properties.getProperty("resolution");

        if (resolutionStr != null) {

            try {
                currentResolution = DisplayResolution.valueOf(resolutionStr);
            } catch (IllegalArgumentException e) {
                currentResolution = DisplayResolution.R_960x540;
            }
        }

        isFullscreen = Boolean.parseBoolean(properties.getProperty("fullscreen", "false"));
        borderless = Boolean.parseBoolean(properties.getProperty("borderless", "false"));

        try {
            selectedScreenIndex = Integer.parseInt(properties.getProperty("selectedScreenIndex", "0"));
        } catch (NumberFormatException e) {
            selectedScreenIndex = 0;
        }
    }

    private void setDefaultSettings() {

        currentResolution = DisplayResolution.R_960x540;

        isFullscreen = false;
        borderless = false;

        selectedScreenIndex = 0;
    }

    private void saveSettings() {

        properties.setProperty("resolution", currentResolution.name());
        properties.setProperty("fullscreen", String.valueOf(isFullscreen));
        properties.setProperty("volume", String.valueOf(VOLUME_MUSIC));
        properties.setProperty("borderless", String.valueOf(borderless));
        properties.setProperty("selectedScreenIndex", String.valueOf(selectedScreenIndex));

        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Configurações Gráficas");
        } catch (IOException e) {
            System.err.println("Erro ao salvar configurações: " + e.getMessage());
        }
    }
}