package org.oskar.modules.file;

import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

/**
 * @author Oskar Veerhoek
 */
public class FileSystem implements GameModule {
    private GameWorld gameWorld;
    public FileSystem() {}

    @Override
    public void create(GameWorld gameWorld) {
        gameWorld.getLoggingSystem().info(FileSystem.class, "Creating file system");
        this.gameWorld = gameWorld;
    }

    @Override
    public void destroy() {
        gameWorld.getLoggingSystem().info(FileSystem.class, "Destroying file system");
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
