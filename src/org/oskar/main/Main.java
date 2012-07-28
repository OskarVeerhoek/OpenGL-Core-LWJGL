package org.oskar.main;

import org.oskar.world.GameWorld;

/**
 * @author Oskar Veerhoek
 */
public class Main {
    public static void main(String args[]) {
        GameWorld gameWorld = new GameWorld();
        gameWorld.create();
        gameWorld.run();
        gameWorld.destroy();
    }
}
