package org.oskar.modules;

import org.oskar.world.GameWorld;

/**
 * @author Oskar Veerhoek
 */
public interface GameModule {
    /**
     * Creates the module in the specified game world.
     * @param gameWorld the game world
     */
    public void create(GameWorld gameWorld);

    /**
     * Destroys the module.
     */
    public void destroy();

    /**
     * @return the game world in which the module was created
     */
    public GameWorld getGameWorld();
}
