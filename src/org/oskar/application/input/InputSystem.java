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

package org.oskar.application.input;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.oskar.GameModule;
import org.oskar.GameWorld;

public class InputSystem implements GameModule {
    private GameWorld gameWorld;
    private Controller gamepad;

    @Override
    public void create(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        gameWorld.info(InputSystem.class, "Creating input system");
        for (Controller c : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            if (c.getType() == Controller.Type.GAMEPAD) {
                gamepad = c;
                gameWorld.debug(InputSystem.class, "Found gamepad " + gamepad);
            }
        }
        gameWorld.info(InputSystem.class, "Done creating input system");
    }

    public boolean isButtonPressed(GamepadButton button) {
        return gamepad.getComponent(button.getIdentifier()).getPollData() == 1;
    }

    public boolean areControllersFound() {
        return gamepad == null;
    }

    public String keyToString(int key) {
        return Keyboard.getKeyName(key);
    }

    public Vector2f getMousePosition() {
        return new Vector2f(Mouse.getX(), Mouse.getY());
    }

    public boolean isKeyBeingPressed(char key) {
        return Keyboard.isKeyDown(Keyboard.getKeyIndex(String.valueOf(key)));
    }

    public boolean isKeyBeingPressed(String key) {
        return Keyboard.isKeyDown(Keyboard.getKeyIndex(key));
    }

    public void update() {
        gamepad.poll();
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                gameWorld.getLogicSystem().sendKeyInput(Keyboard.getEventKey());
            }
        }
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                gameWorld.getLogicSystem().sendMouseInput(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
            }
        }
    }

    @Override
    public void destroy() {
        gameWorld.info(InputSystem.class, "Destroying input system");
        gameWorld.info(InputSystem.class, "Done destroying input system");
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
