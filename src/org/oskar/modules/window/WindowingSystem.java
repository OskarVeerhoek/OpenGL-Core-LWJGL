package org.oskar.modules.window;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

/**
 * Handling all the rendering.
 *
 * @author Oskar Veerhoek
 */
public class WindowingSystem implements GameModule {

    private GameWorld gameWorld;

    @Override
    public void create(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.gameWorld.info(WindowingSystem.class, "Creating windowing system");
        try {
            this.gameWorld.debug(WindowingSystem.class, "Setting display mode to WINDOW_WIDTH, WINDOW_HEIGHT");
            Display.setDisplayMode(new DisplayMode(gameWorld.getIntegerProperty("WINDOW_WIDTH"), gameWorld.getIntegerProperty("WINDOW_HEIGHT")));
            this.gameWorld.debug(WindowingSystem.class, "Enabling VSync");
            Display.setVSyncEnabled(true);
            this.gameWorld.debug(WindowingSystem.class, "Setting window title to WINDOW_TITLE");
            Display.setTitle(gameWorld.getStringProperty("WINDOW_TITLE"));
            this.gameWorld.debug(WindowingSystem.class, "Settings window.resizable to true");
            Display.setResizable(true);
            this.gameWorld.debug(WindowingSystem.class, "Creating a display with a 3.2 OpenGL core profile context");
            Display.create(new PixelFormat(), new ContextAttribs(3, 2).withDebug(true).withProfileCore(true));
        } catch (LWJGLException e) {
            this.gameWorld.fatal(WindowingSystem.class, e);
            e.printStackTrace();
        }
        this.gameWorld.info(WindowingSystem.class, "Done creating windowing system");
    }

    @Override
    public void destroy() {
        gameWorld.info(WindowingSystem.class, "Destroying windowing system");
        gameWorld.debug(WindowingSystem.class, "Destroying display");
        Display.destroy();
        gameWorld.info(WindowingSystem.class, "Done destroying windowing system");
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public void update() {
        if (Display.isCloseRequested()) {
            gameWorld.setFlaggedForDestruction(true);
        }
        if (Display.wasResized()) {
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        }
        Display.update();
    }
}
