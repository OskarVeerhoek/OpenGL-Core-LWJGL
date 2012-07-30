package org.oskar.world;

import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;
import org.oskar.modules.file.FileSystem;
import org.oskar.modules.rendering.RenderingSystem;
import org.oskar.modules.resources.ResourceSystem;
import org.oskar.modules.window.WindowingSystem;
import org.w3c.dom.DOMConfiguration;

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
        info(GameWorld.class, "Destroying game world");
        renderingSystem.destroy();
        windowingSystem.destroy();
        resourceSystem.destroy();
        fileSystem.destroy();
    }

    public void debug(Class sender, String log) {
        Logger.getLogger(sender).debug(log);
    }

    public void info(Class sender, String log) {
        Logger.getLogger(sender).info(log);
    }

    public void warn(Class sender, String log) {
        Logger.getLogger(sender).warn(log);
    }

    public void fatal(Class sender, Exception e) {
        Logger.getLogger(sender).fatal("", e);
        destroy();
    }

    public void fatal(Class sender, String log, Exception e) {
        Logger.getLogger(sender).fatal(log, e);
        destroy();
    }

    public void fatal(Class sender, String log) {
        Logger.getLogger(sender).fatal(log);
        destroy();
    }

    public void error(Class sender, Exception e) {
        Logger.getLogger(sender).error("", e);
    }

    public void error(Class sender, String log, Exception e) {
        Logger.getLogger(sender).error(log, e);
    }

    public void error(Class sender, String log) {
        Logger.getLogger(sender).error(log);
    }

    public void create() {
        info(GameWorld.class, "Creating logging system");
        BasicConfigurator.configure();
        info(GameWorld.class, "Creating game world");
        debug(GameWorld.class, "Setting properties");
        setProperty("WINDOW_TITLE", "Core OpenGL - Java w/ LWJGL");
        setProperty("WINDOW_WIDTH", 640);
        setProperty("WINDOW_HEIGHT", 480);
        setProperty("RESOURCE_VERTEX_SHADER", "res/shader.vs");
        setProperty("RESOURCE_FRAGMENT_SHADER", "res/shader.fs");
        setProperty("RESOURCE_SAMPLE_IMAGE", "res/sample.png");
        fileSystem.create(this);
        resourceSystem.create(this);
        windowingSystem.create(this);
        renderingSystem.create(this);
    }

    public void setProperty(String key, String value) {
        debug(GameWorld.class, "Setting " + key + " to " + value);
        stringProperties.put(key, value);
    }

    public void setProperty(String key, Integer value) {
        debug(GameWorld.class, "Setting " + key + " to " + value);
        integerProperties.put(key, value);
    }

    public Integer getIntegerProperty(String key) {
        if (!integerProperties.containsKey(key)) {
            error(GameWorld.class, "Key " + key + " does not exist.");
            return null;
        } else {
            return integerProperties.get(key);
        }
    }

    public String getStringProperty(String key) {
        if (!stringProperties.containsKey(key)) {
            error(GameWorld.class, "Key " + key + " does not exist.");
            return null;
        } else {
            return stringProperties.get(key);
        }
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

    public void run() {
        while (!flaggedForDestruction.get()) {
            renderingSystem.update();
            windowingSystem.update();
        }
    }
}
