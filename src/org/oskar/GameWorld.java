/*
 * Copyright (c) 2012, Oskar Veerhoek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package org.oskar;

import org.apache.log4j.Logger;
import org.oskar.application.file.FileSystem;
import org.oskar.application.input.InputSystem;
import org.oskar.logic.LogicSystem;
import org.oskar.view.RenderingSystem;
import org.oskar.application.resources.ResourceSystem;
import org.oskar.application.window.WindowingSystem;

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
    private LogicSystem logicSystem = new LogicSystem();
    private InputSystem inputSystem = new InputSystem();
    private Map<String, String> stringProperties = new HashMap<String, String>();
    private Map<String, Integer> integerProperties = new HashMap<String, Integer>();
    private boolean isCreated = false;
    private AtomicBoolean flaggedForDestruction = new AtomicBoolean(false);

    public void setFlaggedForDestruction(boolean value) {
        this.flaggedForDestruction.set(value);
    }

    public boolean isFlaggedForDestruction() {
        return flaggedForDestruction.get();
    }

    /**
     * Sets the "created" state to true.
     */
    public GameWorld() {
        isCreated = true;
    }

    /**
     * Destroys all the modules that the game world uses.
     */
    public void destroy() {
        info(GameWorld.class, "Destroying game world");
        inputSystem.destroy();
        logicSystem.destroy();
        renderingSystem.destroy();
        windowingSystem.destroy();
        resourceSystem.destroy();
        fileSystem.destroy();
        isCreated = false;
        info(GameWorld.class, "Done destroying game world");
    }

    /**
     * Prints out a debug log.
     * @param sender the class from which the log is sent
     * @param log the contents of the log
     */
    public void debug(Class sender, String log) {
        Logger.getLogger(sender).debug(log);
    }

    /**
     * Prints out an info log.
     * @param sender the class from which the log is sent
     * @param log the contents of the log
     */
    public void info(Class sender, String log) {
        Logger.getLogger(sender).info(log);
    }

    /**
     * Prints out a warning log.
     * @param sender the class from which the log is sent
     * @param log the contents of the log
     */
    public void warn(Class sender, String log) {
        Logger.getLogger(sender).warn(log);
    }

    /**
     * Prints out a fatal exception and destroys the game world.
     * @param sender the class from which the log is sent
     * @param e the exception that occurred
     */
    public void fatal(Class sender, Exception e) {
        Logger.getLogger(sender).fatal("", e);
        setFlaggedForDestruction(true);
    }

    /**
     * Prints out a fatal exception with a log and destroys the game world.
     * @param sender the class from which the log was sent
     * @param log the contents of the log
     * @param e the exception that occurred
     */
    public void fatal(Class sender, String log, Exception e) {
        Logger.getLogger(sender).fatal(log, e);
        setFlaggedForDestruction(true);
    }

    /**
     * Prints out a fatal log and destroys the game world.
     * @param sender the class from which the log was sent
     * @param log the contents of the log
     */
    public void fatal(Class sender, String log) {
        Logger.getLogger(sender).fatal(log);
        setFlaggedForDestruction(true);
    }

    /**
     * Prints out an exception.
     * @param sender the class from which the log is sent
     * @param e the exception that occurred
     */
    public void error(Class sender, Exception e) {
        Logger.getLogger(sender).error("", e);
    }

    /**
     * Prints out an exception with a log.
     * @param sender the class from which the log is sent
     * @param log the contents of the log
     * @param e the exception that occurred
     */
    public void error(Class sender, String log, Exception e) {
        Logger.getLogger(sender).error(log, e);
    }

    /**
     * Prints out an error log.
     * @param sender the class from which the log is sent
     * @param log the contents of the log
     */
    public void error(Class sender, String log) {
        Logger.getLogger(sender).error(log);
    }

    /**
     * Sets the appropriate properties and initialises the following modules:
     * - File System
     * - Resource System
     * - Windowing System
     * - Rendering System
     */
    public void create() {
        info(GameWorld.class, "Creating game world");
        debug(GameWorld.class, "Setting properties");
        setProperty("WINDOW_TITLE", "Core OpenGL - Java w/ LWJGL");
        setProperty("WINDOW_WIDTH", 640);
        setProperty("WINDOW_HEIGHT", 480);
        setProperty("RESOURCE_VERTEX_SHADER", "res/shader.vs");
        setProperty("RESOURCE_FRAGMENT_SHADER", "res/shader.fs");
        fileSystem.create(this);
        resourceSystem.create(this);
        windowingSystem.create(this);
        renderingSystem.create(this);
        logicSystem.create(this);
        inputSystem.create(this);
        info(GameWorld.class, "Done creating game world");
    }

    public void setProperty(String key, String value) {
        debug(GameWorld.class, "Setting " + key + " to \"" + value + "\"");
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

    public LogicSystem getLogicSystem() {
        return logicSystem;
    }

    public InputSystem getInputSystem() {
        return inputSystem;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public ResourceSystem getResourceSystem() {
        return resourceSystem;
    }

    public void run() {
        while (!flaggedForDestruction.get()) {
            inputSystem.update();
            logicSystem.update();
            renderingSystem.update();
            windowingSystem.update();
        }
    }
}
