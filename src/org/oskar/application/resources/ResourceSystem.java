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

package org.oskar.application.resources;

import org.oskar.GameModule;
import org.oskar.GameWorld;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oskar Veerhoek
 */
public class ResourceSystem implements GameModule {

    private GameWorld gameWorld;
    private Map<String, String> textFiles = new HashMap<String, String>();
    private Map<String, ByteBuffer> textureFiles = new HashMap<String, ByteBuffer>();

    /**
     * @param key the key of the text file
     * @return the string contents of the stored text file, or null if the key does not exist
     */
    public String getTextFileContent(String key) {
        return textFiles.get(key);
    }

    /**
     * @param key the key of the text file
     * @return the ByteBuffer contents of the stored texture, or null if the key does not exist
     */
    public ByteBuffer getTextureContent(String key) {
        return textureFiles.get(key);
    }

    @Override
    public void create(GameWorld gameWorld) {
        gameWorld.info(ResourceSystem.class, "Creating resource system");
        this.gameWorld = gameWorld;
        textFiles.put("RESOURCE_VERTEX_SHADER", gameWorld.getFileSystem().loadFileToString(new File(gameWorld.getStringProperty("RESOURCE_VERTEX_SHADER")), true));
        textFiles.put("RESOURCE_FRAGMENT_SHADER", gameWorld.getFileSystem().loadFileToString(new File(gameWorld.getStringProperty("RESOURCE_FRAGMENT_SHADER")), true));
        gameWorld.info(ResourceSystem.class, "Done creating resource system");
    }

    @Override
    public void destroy() {
        gameWorld.info(ResourceSystem.class, "Destroying resource system");
        gameWorld.info(ResourceSystem.class, "Done destroying resource system");
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
