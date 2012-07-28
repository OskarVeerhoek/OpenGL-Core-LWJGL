package org.oskar.modules.logging;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

/**
 * @author Oskar Veerhoek
 */
public class LoggingSystem implements GameModule {

    private GameWorld gameWorld;

    public LoggingSystem() {

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
        gameWorld.setFlaggedForDestruction(true);
    }

    public void fatal(Class sender, String log, Exception e) {
        Logger.getLogger(sender).fatal(log, e);
        gameWorld.setFlaggedForDestruction(true);
    }

    public void fatal(Class sender, String log) {
        Logger.getLogger(sender).fatal(log);
        gameWorld.setFlaggedForDestruction(true);
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

    @Override
    public void create(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        BasicConfigurator.configure();

    }

    @Override
    public void destroy() {
        gameWorld.getLoggingSystem().info(LoggingSystem.class, "Destroying logging system");
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
