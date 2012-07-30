#version 150 core

layout(location = 0) in vec4 vertex_position;
layout(location = 1) in vec3 vertex_colour;

smooth out vec3 varying_colour;

void main()
{
    varying_colour = vertex_colour;
    gl_Position = vertex_position;
}