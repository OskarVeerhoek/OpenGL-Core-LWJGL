#version 150 core

smooth in vec4 fragment_colour;

void main()
{
    gl_FragColor = fragment_colour;
}