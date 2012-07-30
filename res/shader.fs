#version 150 core

smooth in vec3 varying_colour;

out vec4 fragment_colour;

void main()
{
    fragment_colour = vec4(varying_colour, 1);
}