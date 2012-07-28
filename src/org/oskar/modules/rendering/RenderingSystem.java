package org.oskar.modules.rendering;

import org.lwjgl.BufferUtils;
import org.oskar.modules.GameModule;
import org.oskar.world.GameWorld;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

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
    private final int VERTEX_TEXTURE_COORDINATE = 2;
    private int vbo;
    private int vao;
    private int vertexShader;
    private int fragmentShader;
    private int shaderProgram;
    private int sampleImage;
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
        gameWorld.debug(RenderingSystem.class, "Creating VBO");
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
            // Texture Coordinates
                +0.0f, +0.0f,
                +1.0f, +0.0f,
                +1.0f, +1.0f
        });
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(VERTEX_POSITION);
        glEnableVertexAttribArray(VERTEX_COLOUR);
        glEnableVertexAttribArray(VERTEX_TEXTURE_COORDINATE);
        glVertexAttribPointer(VERTEX_POSITION, 2, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(VERTEX_COLOUR, 3, GL_FLOAT, false, 0, 24);
        glVertexAttribPointer(VERTEX_TEXTURE_COORDINATE, 2, GL_FLOAT, false, 0, 60);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        checkForErrors();
    }

    private void createShaders() {
        gameWorld.debug(RenderingSystem.class, "Creating shaders");
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, gameWorld.getResourceSystem().getTextFileContent("RESOURCE_VERTEX_SHADER"));
        glCompileShader(vertexShader);
        gameWorld.debug(RenderingSystem.class, "OpenGL vertex shader info log: " + glGetShaderInfoLog(vertexShader, 2056).split("\n")[0]);
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, gameWorld.getResourceSystem().getTextFileContent("RESOURCE_FRAGMENT_SHADER"));
        glCompileShader(fragmentShader);
        gameWorld.debug(RenderingSystem.class, "OpenGL fragment shader info log: " + glGetShaderInfoLog(fragmentShader, 2056).split("\n")[0]);
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        glUseProgram(shaderProgram);
        gameWorld.debug(RenderingSystem.class, "Setting \"texture\" uniform to 0");
        glActiveTexture(GL_TEXTURE0);
        glUniform1i(glGetUniformLocation(shaderProgram, "texture"), 0);
        glValidateProgram(shaderProgram);
        gameWorld.debug(RenderingSystem.class, "OpenGL shader program info log: " + glGetProgramInfoLog(shaderProgram, 2056).split("\n")[0]);
        glUseProgram(0);
        checkForErrors();
    }

    public void createTextures() {
        gameWorld.debug(RenderingSystem.class, "Creating textures");
        glActiveTexture(GL_TEXTURE0);
        sampleImage = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, sampleImage);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        String imagePath = gameWorld.getStringProperty("RESOURCE_SAMPLE_IMAGE");
        ByteBuffer imageData = gameWorld.getResourceSystem().loadImage(new File(imagePath));
        imageData.flip();
        int width = gameWorld.getIntegerProperty("RESOURCE_sample.png_WIDTH");
        int height = gameWorld.getIntegerProperty("RESOURCE_sample.png_HEIGHT");
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void create(GameWorld gameWorld) {
        gameWorld.info(RenderingSystem.class, "Creating rendering system");
        this.gameWorld = gameWorld;
        gameWorld.debug(RenderingSystem.class, "Checking OpenGL version");
        if (glGetString(GL_VERSION).startsWith("3.2")) {
            // TODO: Optimize glGetString calls
            gameWorld.debug(RenderingSystem.class, "OpenGL version is correct: " + glGetString(GL_VERSION));
        } else {
            gameWorld.fatal(RenderingSystem.class, "Wrong OpenGL version: " + glGetString(GL_VERSION));
        }
        createBuffers();
        createShaders();
        createTextures();
    }

    @Override
    public void destroy() {
        gameWorld.info(RenderingSystem.class, "Destroying rendering system");
        gameWorld.debug(RenderingSystem.class, "Destroying textures");
        glBindTexture(GL_TEXTURE_2D, 0);
        glDeleteTextures(sampleImage);
        gameWorld.debug(RenderingSystem.class, "Destroying VBO");
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vbo);
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
        glBindTexture(GL_TEXTURE_2D, sampleImage);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindTexture(GL_TEXTURE_2D, 0);
        glUseProgram(0);
        glBindVertexArray(0);
        checkForErrors();
    }

}
