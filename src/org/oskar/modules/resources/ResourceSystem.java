package org.oskar.modules.resources;

import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oskar Veerhoek
 */
public class ResourceSystem implements GameModule {

    private GameWorld gameWorld;
    private Map<String, String> textFiles = new HashMap<String, String>();

    public String getTextFileContent(String key) {
        return textFiles.get(key);
    }

    public String loadFileToString(File file) {
        gameWorld.getLoggingSystem().debug(ResourceSystem.class, "Loading " + file.toString() + " to string");
        StringBuilder fileSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    file));
            String line;
            while ((line = reader.readLine()) != null) {
                fileSource.append(line).append('\n');
            }
            reader.close();
        } catch (IOException e) {
            gameWorld.getLoggingSystem().fatal(ResourceSystem.class, e);
        }
        return fileSource.toString();
    }

    @Override
    public void create(GameWorld gameWorld) {
        gameWorld.getLoggingSystem().info(ResourceSystem.class, "Creating resource system");
        this.gameWorld = gameWorld;
        textFiles.put("RESOURCE_VERTEX_SHADER", loadFileToString(new File(gameWorld.getStringProperty("RESOURCE_VERTEX_SHADER"))));
        textFiles.put("RESOURCE_FRAGMENT_SHADER", loadFileToString(new File(gameWorld.getStringProperty("RESOURCE_FRAGMENT_SHADER"))));
    }

    @Override
    public void destroy() {
        gameWorld.getLoggingSystem().info(ResourceSystem.class, "Destroying resource system");
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
