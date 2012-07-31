package org.oskar.modules.rendering;

import org.lwjgl.BufferUtils;
import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Handles all the rendering.
 *
 * @author Oskar Veerhoek
 */
public class RenderingSystem implements GameModule {
    private GameWorld gameWorld;
    private final int VERTEX_POSITION = 0;
    private final int VERTEX_COLOUR = 1;
    private int vbo;
    private int ibo;
    private int vao;
    private int vertexShader;
    private int fragmentShader;
    private int shaderProgram;
    private int sampleImage;
    public RenderingSystem() {

    }

    private ShortBuffer asShortBuffer(short[] values) {
        ShortBuffer buffer = BufferUtils.createShortBuffer(values.length);
        buffer.put(values);
        return buffer;
    }

    private ShortBuffer asFlippedShortBuffer(short[] values) {
        ShortBuffer buffer = BufferUtils.createShortBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;
    }

    private IntBuffer asIntBuffer(int[] values) {
        IntBuffer buffer = BufferUtils.createIntBuffer(values.length);
        buffer.put(values);
        return buffer;
    }

    private IntBuffer asFlippedIntBuffer(int[] values) {
        IntBuffer intBuffer = asIntBuffer(values);
        intBuffer.flip();
        return intBuffer;
    }

    private FloatBuffer asFloatBuffer(float[] values) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(values.length);
        floatBuffer.put(values);
        return floatBuffer;
    }

    private FloatBuffer asFlippedFloatBuffer(float[] values) {
        FloatBuffer floatBuffer = asFloatBuffer(values);
        floatBuffer.flip();
        return floatBuffer;
    }

    private void checkForErrors() {
        int error = glGetError();
        switch (error) {
            case GL_NO_ERROR:
                break;
            case GL_INVALID_ENUM:
                gameWorld.error(RenderingSystem.class, "OpenGL error: GL_INVALID_ENUM");
                break;
            case GL_INVALID_VALUE:
                gameWorld.error(RenderingSystem.class, "OpenGL error: GL_INVALID_VALUE");
                break;
            case GL_INVALID_OPERATION:
                gameWorld.error(RenderingSystem.class, "OpenGL error: GL_INVALID_OPERATION");
                break;
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                gameWorld.error(RenderingSystem.class, "OpenGL error: GL_INVALID_FRAMEBUFFER_OPERATION");
                break;
            case GL_OUT_OF_MEMORY:
                gameWorld.error(RenderingSystem.class, "OpenGL error: GL_OUT_OF_MEMORY");
                break;
        }
    }

    private void createBuffers() {
        gameWorld.debug(RenderingSystem.class, "Creating VAO");
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        gameWorld.debug(RenderingSystem.class, "Creating IBO");
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        IntBuffer indexData = asIntBuffer(new int[]{
                0, 1, 2,
                0, 2, 3
        });
        indexData.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        gameWorld.debug(RenderingSystem.class, "Creating VBO");
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer vertexData = asFlippedFloatBuffer(new float[] {
            // Positions
                -1.0f, -1.0f,
                +1.0f, -1.0f,
                +1.0f, +1.0f,
                -1.0f, +1.0f,
            // Colours
                +1.0f, +0.0f, +0.0f,
                +0.0f, +1.0f, +0.0f,
                +0.0f, +0.0f, +1.0f,
                +1.0f, +1.0f, +1.0f
        });
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(VERTEX_POSITION);
        glEnableVertexAttribArray(VERTEX_COLOUR);
        glVertexAttribPointer(VERTEX_POSITION, 2, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(VERTEX_COLOUR, 3, GL_FLOAT, false, 0, 32);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        checkForErrors();
    }

    private void createShaders() {
        gameWorld.debug(RenderingSystem.class, "Creating shaders");
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, gameWorld.getResourceSystem().getTextFileContent("RESOURCE_VERTEX_SHADER"));
        glCompileShader(vertexShader);
        if (glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            gameWorld.debug(RenderingSystem.class, "OpenGL vertex shader info log: " + glGetShaderInfoLog(fragmentShader, 2056));
            gameWorld.setFlaggedForDestruction(true);
        }
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, gameWorld.getResourceSystem().getTextFileContent("RESOURCE_FRAGMENT_SHADER"));
        glCompileShader(fragmentShader);
        if (glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            gameWorld.debug(RenderingSystem.class, "OpenGL fragment shader info log: " + glGetShaderInfoLog(fragmentShader, 2056));
            gameWorld.setFlaggedForDestruction(true);
        }
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        if (glGetProgram(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            gameWorld.debug(RenderingSystem.class, "OpenGL shader program info log: " + glGetProgramInfoLog(shaderProgram, 2056));
            gameWorld.setFlaggedForDestruction(true);
        }
        glUseProgram(shaderProgram);
        glValidateProgram(shaderProgram);
        glUseProgram(0);
        checkForErrors();
    }

    @Override
    public void create(GameWorld gameWorld) {
        gameWorld.info(RenderingSystem.class, "Creating rendering system");
        this.gameWorld = gameWorld;
        gameWorld.debug(RenderingSystem.class, "Checking OpenGL version");
        double openglVersion = Double.parseDouble(glGetString(GL_VERSION).substring(0, 3));
        if (openglVersion >= 3.2) {
            gameWorld.debug(RenderingSystem.class, "OpenGL version is correct: " + glGetString(GL_VERSION));
        } else {
            gameWorld.fatal(RenderingSystem.class, "Wrong OpenGL version: " + glGetString(GL_VERSION));
        }
        createBuffers();
        createShaders();
    }

    @Override
    public void destroy() {
        gameWorld.info(RenderingSystem.class, "Destroying rendering system");
        gameWorld.debug(RenderingSystem.class, "Destroying VBO");
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vbo);
        checkForErrors();
        gameWorld.debug(RenderingSystem.class, "Destroying IBO");
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(ibo);
        checkForErrors();
        gameWorld.debug(RenderingSystem.class, "Destroying shader program");
        glUseProgram(0);
        glDeleteProgram(shaderProgram);
        checkForErrors();
        gameWorld.debug(RenderingSystem.class, "Destroying vertex shader");
        glDeleteShader(vertexShader);
        checkForErrors();
        gameWorld.debug(RenderingSystem.class, "Destroing fragment shader");
        glDeleteShader(fragmentShader);
        checkForErrors();
        gameWorld.debug(RenderingSystem.class, "Destroying VAO");
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
        checkForErrors();
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public void update() {
        glClear(GL_COLOR_BUFFER_BIT);
        glBindVertexArray(vao);
        glUseProgram(shaderProgram);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glUseProgram(0);
        glBindVertexArray(0);
        checkForErrors();
    }

}
