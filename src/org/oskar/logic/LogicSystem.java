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

package org.oskar.logic;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.oskar.GameModule;
import org.oskar.GameWorld;
import org.oskar.application.input.GamepadButton;

public class LogicSystem implements GameModule {
    private GameWorld gameWorld;

    /**
     * Tells the logic system that a key was pressed.
     * @param key the key that was pressed
     */
    public void sendKeyInput(int key) {
        gameWorld.debug(LogicSystem.class, "Processing Key " + gameWorld.getInputSystem().keyToString(key) + " which has been pressed");
        if (key == Keyboard.KEY_Q) {
            gameWorld.getRenderingSystem().setIsDrawing(false);
        } else if (key == Keyboard.KEY_S) {
            gameWorld.getRenderingSystem().setIsDrawing(true);
        }
    }

    /**
     * Tells the logic system that a mouse button was pressed.
     * @param mouseButton the mouse button that was pressed
     */
    public void sendMouseInput(int mouseButton, int x, int y) {

    }

    @Override
    public void create(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        gameWorld.info(LogicSystem.class, "Creating logic system");
        gameWorld.info(LogicSystem.class, "Done creating logic system");
    }

    @Override
    public void destroy() {
        gameWorld.info(LogicSystem.class, "Destroying logic system");
        gameWorld.info(LogicSystem.class, "Done destroying logic system");
    }

    public void update() {
        Vector2f mousePosition = gameWorld.getInputSystem().getMousePosition();
        Vector3f bias = new Vector3f();
        bias.x = mousePosition.x / gameWorld.getIntegerProperty("WINDOW_WIDTH");
        bias.y = mousePosition.y / gameWorld.getIntegerProperty("WINDOW_HEIGHT");
        bias.z = (bias.x + bias.y) / 2;
        if (gameWorld.getInputSystem().isButtonPressed(GamepadButton.LB)) {
            bias.scale(0.8f);
        }
        if (gameWorld.getInputSystem().isButtonPressed(GamepadButton.RB)) {
            bias.scale(1.25f);
        }
        gameWorld.getRenderingSystem().setBias(bias.x, bias.y, bias.z);
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
