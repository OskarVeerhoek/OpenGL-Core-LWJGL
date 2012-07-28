#version 150 core

uniform sampler2D texture;

smooth in vec4 fragment_colour;
smooth in vec4 fragment_texture_coordinate;

void main()
{
    //gl_FragColor = texture2D(texture, fragment_texture_coordinate.st);
    gl_FragColor = fragment_colour;
}