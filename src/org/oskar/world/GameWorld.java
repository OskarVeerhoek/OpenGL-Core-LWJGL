package org.oskar.world;

import org.oskar.modules.file.FileSystem;
import org.oskar.modules.logging.LoggingSystem;
import org.oskar.modules.rendering.RenderingSystem;
import org.oskar.modules.resources.ResourceSystem;
import org.oskar.modules.window.WindowingSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Oskar Veerhoek
 */
public class GameWorld {

    private WindowingSystem windowingSystem = new WindowingSystem();
    private RenderingSystem renderingSystem = new RenderingSystem();
    private FileSystem fileSystem = new FileSystem();
    private ResourceSystem resourceSystem = new ResourceSystem();
    private LoggingSystem loggingSystem = new LoggingSystem();
    private Properties properties = new Properties();
    private Map<String, String> stringProperties = new HashMap<String, String>();
    private Map<String, Integer> integerProperties = new HashMap<String, Integer>();
    private boolean isCreated = false;
    private AtomicBoolean flaggedForDestruction = new AtomicBoolean(false);

    public void setFlaggedForDestruction(boolean value) {
        this.flaggedForDestruction.set(value);
    }

    public boolean getFlaggedForDestruction() {
        return flaggedForDestruction.get();
    }

    public GameWorld() {
        isCreated = true;
    }

    public void destroy() {
        loggingSystem.info(GameWorld.class, "Destroying game world");
        renderingSystem.destroy();
        windowingSystem.destroy();
        resourceSystem.destroy();
        fileSystem.destroy();
        loggingSystem.destroy();
    }

    public void create() {
        loggingSystem.create(this);
        loggingSystem.info(GameWorld.class, "Creating game world");
        loggingSystem.debug(GameWorld.class, "Setting properties");
        setProperty("WINDOW_TITLE", "Game Coding Complete - Java w/ LWJGL");
        setProperty("WINDOW_WIDTH", 640);
        setProperty("WINDOW_HEIGHT", 480);
        setProperty("RESOURCE_VERTEX_SHADER", "res/shader.vs");
        setProperty("RESOURCE_FRAGMENT_SHADER", "res/shader.fs");
        fileSystem.create(this);
        resourceSystem.create(this);
        windowingSystem.create(this);
        renderingSystem.create(this);
    }

    public void setProperty(String key, String value) {
        loggingSystem.debug(GameWorld.class, "Setting " + key + " to " + value);
        stringProperties.put(key, value);
    }

    public void setProperty(String key, Integer value) {
        loggingSystem.debug(GameWorld.class, "Setting " + key + " to " + value);
        integerProperties.put(key, value);
    }

    public Integer getIntegerProperty(String key) {
        return integerProperties.get(key);
    }

    public String getStringProperty(String key) {
        return stringProperties.get(key);
    }

    public boolean isCreated() {
        return isCreated;
    }

    public RenderingSystem getRenderingSystem() {
        return renderingSystem;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public ResourceSystem getResourceSystem() {
        return resourceSystem;
    }

    public LoggingSystem getLoggingSystem() {
        return loggingSystem;
    }

    public void run() {
        while (!flaggedForDestruction.get()) {
            renderingSystem.update();
            windowingSystem.update();
        }
    }
}
