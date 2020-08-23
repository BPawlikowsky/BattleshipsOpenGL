#version 330 core

layout (location = 0) out vec4 color;

in DATA
{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform float time;
void main()
{
	if (time < 0.0 && time > 1.0)
		discard;
	color = texture(tex, fs_in.tc);
    color.w = color.w - time;
}