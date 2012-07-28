#version 150 core

uniform sampler2D texture;

smooth in vec4 fragment_colour;
smooth in vec4 fragment_texture_coordinate;

void main()
{
    gl_FragColor = mix(fragment_colour, texture2D(texture, fragment_texture_coordinate.st), 0.5);
    //gl_FragColor = fragment_colour;
}