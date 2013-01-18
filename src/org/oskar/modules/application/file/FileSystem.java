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

package org.oskar.modules.application.file;

import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Oskar Veerhoek
 */
public class FileSystem implements GameModule {
    private GameWorld gameWorld;

    public FileSystem() {}

    public String loadFileToString(File file, boolean addNewline) {
        gameWorld.debug(FileSystem.class, "Loading " + file.toString() + " to string");
        StringBuilder fileSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (addNewline)
                    fileSource.append(line).append('\n');
                else
                    fileSource.append(line);
            }
            reader.close();
        } catch (IOException e) {
            gameWorld.fatal(FileSystem.class, e);
        }
        return fileSource.toString();
    }

    @Override
    public void create(GameWorld gameWorld) {
        gameWorld.info(FileSystem.class, "Creating file system");
        this.gameWorld = gameWorld;
        gameWorld.info(FileSystem.class, "Done creating file system");
    }

    @Override
    public void destroy() {
        gameWorld.info(FileSystem.class, "Destroying file system");
        gameWorld.info(FileSystem.class, "Done destroying file system");
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
