#version 150 core

layout(location = 0) in vec4 vertex_position;
layout(location = 1) in vec4 vertex_colour;
layout(location = 2) in vec4 vertex_texture_coordinate;

uniform sampler2D texture;

smooth out vec4 fragment_texture_coordinate;
smooth out vec4 fragment_colour;

void main()
{
    fragment_texture_coordinate = vertex_texture_coordinate;
    fragment_colour = vertex_colour;
    gl_Position = vertex_position;
}