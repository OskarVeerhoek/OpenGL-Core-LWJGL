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
 *
 * Copyright (c) 2002-2007 Lightweight Java Game Library Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'Light Weight Java Game Library' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2008-2010, Matthias Mann
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Matthias Mann nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
