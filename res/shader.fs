#version 150 core

uniform sampler2D texture;

smooth in vec3 varying_colour;
smooth in vec2 varying_texture_coordinate;

out vec4 fragment_colour;

void main()
{
    fragment_colour = texture(texture, varying_texture_coordinate.st);
}