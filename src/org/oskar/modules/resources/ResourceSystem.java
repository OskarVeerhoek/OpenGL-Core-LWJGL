package org.oskar.modules.resources;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Oskar Veerhoek
 */
public class ResourceSystem implements GameModule {

    private GameWorld gameWorld;
    private Map<String, String> textFiles = new HashMap<String, String>();

    public String getTextFileContent(String key) {
        return textFiles.get(key);
    }

    public int loadImage(File file) {
        gameWorld.debug(ResourceSystem.class, "Loading image file " + file.getName());
        if (!file.getName().endsWith("png")) {
            gameWorld.error(ResourceSystem.class, "File is not a PNG file");
            return 0;
        }
        try {
            PNGDecoder decoder = new PNGDecoder(new FileInputStream(file));
            ByteBuffer imageData = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(imageData, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            int texture = GL11.glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) imageData.flip());
            glBindTexture(GL_TEXTURE_2D, 0);
        } catch (IOException e) {
            gameWorld.error(ResourceSystem.class, e);
        }
        return 0;
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
