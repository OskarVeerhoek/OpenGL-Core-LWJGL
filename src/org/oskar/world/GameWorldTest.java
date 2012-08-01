package org.oskar.world;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Oskar Veerhoek
 */
public class GameWorldTest {
    private GameWorld gameWorld = new GameWorld();
    @Before
    public void createGameWorld() {
        gameWorld.create();
    }
    @After
    public void destroyGameWorld() {
        gameWorld.destroy();
    }
    @Test
    public void testFlaggedForDestruction() {
        gameWorld.setFlaggedForDestruction(true);
        Assert.assertEquals(true, gameWorld.getFlaggedForDestruction());
        gameWorld.setFlaggedForDestruction(false);
        Assert.assertEquals(false, gameWorld.getFlaggedForDestruction());
    }
    @Test
    public void testProperties() {
        String stringKey = "09fb8c98gb";
        String integerKey = "vc98b7cv98b7";
        String stringValue = "98vucb98v";
        Integer integerValue = 29587987;
        gameWorld.setProperty(stringKey, stringValue);
        Assert.assertEquals(stringValue, gameWorld.getStringProperty(stringKey));
        gameWorld.setProperty(integerKey, integerValue);
        Assert.assertEquals(integerValue, gameWorld.getIntegerProperty(integerKey));
    }
}
