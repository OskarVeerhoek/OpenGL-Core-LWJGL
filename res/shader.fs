#version 150 core

uniform sampler2D texture;

smooth in vec3 fragment_colour;
smooth in vec2 fragment_texture_coordinate;

void main()
{
    //gl_FragColor = mix(vec4(fragment_colour, 1), texture2D(texture, fragment_texture_coordinate.st), 0.5);
    //gl_FragColor = fragment_colour;
    gl_FragColor = texture2D(texture, fragment_texture_coordinate.st);
}