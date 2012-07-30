#version 150 core

layout(location = 0) in vec4 vertex_position;
layout(location = 1) in vec3 vertex_colour;
layout(location = 2) in vec2 vertex_texture_coordinate;

uniform sampler2D texture;

smooth out vec2 varying_texture_coordinate;
smooth out vec3 varying_colour;

void main()
{
    varying_texture_coordinate = vertex_texture_coordinate;
    varying_colour = vertex_colour;
    gl_Position = vertex_position;
}