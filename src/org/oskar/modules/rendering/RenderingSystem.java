package org.oskar.modules.rendering;

import org.lwjgl.BufferUtils;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
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
    private int vao;
    private int vertexShader;
    private int fragmentShader;
    private int shaderProgram;
    public RenderingSystem() {

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
                gameWorld.getLoggingSystem().error(RenderingSystem.class, "OpenGL error: GL_INVALID_ENUM");
                break;
            case GL_INVALID_VALUE:
                gameWorld.getLoggingSystem().error(RenderingSystem.class, "OpenGL error: GL_INVALID_VALUE");
                break;
            case GL_INVALID_OPERATION:
                gameWorld.getLoggingSystem().error(RenderingSystem.class, "OpenGL error: GL_INVALID_OPERATION");
                break;
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                gameWorld.getLoggingSystem().error(RenderingSystem.class, "OpenGL error: GL_INVALID_FRAMEBUFFER_OPERATION");
                break;
            case GL_OUT_OF_MEMORY:
                gameWorld.getLoggingSystem().error(RenderingSystem.class, "OpenGL error: GL_OUT_OF_MEMORY");
                break;
        }
    }

    private void createBuffers() {
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "Creating VAO");
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "Creating VBO");
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer vertexData = asFlippedFloatBuffer(new float[] {
            // Positions
                -1.0f, -1.0f,
                +1.0f, -1.0f,
                +1.0f, +1.0f,
            // Colours
                +1.0f, +0.0f, +0.0f,
                +0.0f, +1.0f, +0.0f,
                +0.0f, +0.0f, +1.0f
        });
        glBufferData(GL_ARRAY_BUFFER, asFlippedFloatBuffer(new float[]{-1, -1, 1, -1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1}), GL_STATIC_DRAW);
        glEnableVertexAttribArray(VERTEX_POSITION);
        glEnableVertexAttribArray(VERTEX_COLOUR);
        glVertexAttribPointer(VERTEX_POSITION, 2, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(VERTEX_COLOUR, 3, GL_FLOAT, false, 0, 24);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        checkForErrors();
    }

    private void createShaders() {
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "Creating shaders");
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, gameWorld.getResourceSystem().getTextFileContent("RESOURCE_VERTEX_SHADER"));
        glCompileShader(vertexShader);
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "OpenGL vertex shader info log: " + glGetShaderInfoLog(vertexShader, 2056).split("\n")[0]);
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, gameWorld.getResourceSystem().getTextFileContent("RESOURCE_FRAGMENT_SHADER"));
        glCompileShader(fragmentShader);
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "OpenGL fragment shader info log: " + glGetShaderInfoLog(fragmentShader, 2056).split("\n")[0]);
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "OpenGL shader program info log: " + glGetProgramInfoLog(shaderProgram, 2056).split("\n")[0]);
        checkForErrors();
    }

    @Override
    public void create(GameWorld gameWorld) {
        gameWorld.getLoggingSystem().info(RenderingSystem.class, "Creating rendering system");
        this.gameWorld = gameWorld;
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "Checking OpenGL version");
        if (glGetString(GL_VERSION).startsWith("3.2")) {
            // TODO: Optimize glGetString calls
            gameWorld.getLoggingSystem().debug(RenderingSystem.class, "OpenGL version is correct: " + glGetString(GL_VERSION));
        } else {
            gameWorld.getLoggingSystem().fatal(RenderingSystem.class, "Wrong OpenGL version: " + glGetString(GL_VERSION));
        }
        createBuffers();
        createShaders();
    }

    @Override
    public void destroy() {
        gameWorld.getLoggingSystem().info(RenderingSystem.class, "Destroying rendering system");
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "Destroying VBO");
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vbo);
        checkForErrors();
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "Destroying shader program");
        glUseProgram(0);
        glDeleteProgram(shaderProgram);
        checkForErrors();
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "Destroying vertex shader");
        glDeleteShader(vertexShader);
        checkForErrors();
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "Destroing fragment shader");
        glDeleteShader(fragmentShader);
        checkForErrors();
        gameWorld.getLoggingSystem().debug(RenderingSystem.class, "Destroying VAO");
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
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glUseProgram(0);
        glBindVertexArray(0);
        checkForErrors();
    }

}
