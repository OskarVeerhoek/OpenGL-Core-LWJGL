package org.oskar.modules.resources;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

import java.io.*;
import java.nio.ByteBuffer;
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

    public ByteBuffer loadImage(File file) {
        gameWorld.debug(ResourceSystem.class, "Loading image file " + file.getName());
        if (!file.getName().endsWith("png")) {
            gameWorld.error(ResourceSystem.class, "File is not a PNG file");
            return null;
        }
        try {
            PNGDecoder decoder = new PNGDecoder(new FileInputStream(file));
            gameWorld.setProperty("RESOURCE_" + file.getName() + "_WIDTH", decoder.getWidth());
            gameWorld.setProperty("RESOURCE_" + file.getName() + "_HEIGHT", decoder.getHeight());
            ByteBuffer imageData = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(imageData, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            return imageData;
        } catch (IOException e) {
            gameWorld.error(ResourceSystem.class, e);
        }
        return null;
    }

    public String loadFileToString(File file) {
        gameWorld.debug(ResourceSystem.class, "Loading " + file.toString() + " to string");
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
            gameWorld.fatal(ResourceSystem.class, e);
        }
        return fileSource.toString();
    }

    @Override
    public void create(GameWorld gameWorld) {
        gameWorld.info(ResourceSystem.class, "Creating resource system");
        this.gameWorld = gameWorld;
        textFiles.put("RESOURCE_VERTEX_SHADER", loadFileToString(new File(gameWorld.getStringProperty("RESOURCE_VERTEX_SHADER"))));
        textFiles.put("RESOURCE_FRAGMENT_SHADER", loadFileToString(new File(gameWorld.getStringProperty("RESOURCE_FRAGMENT_SHADER"))));
    }

    @Override
    public void destroy() {
        gameWorld.info(ResourceSystem.class, "Destroying resource system");
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
