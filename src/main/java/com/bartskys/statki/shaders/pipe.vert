#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix = mat4(1.0);
uniform mat4 ml_matrix = mat4(1.0);
uniform int top;

out DATA
{
    vec2 tc;
    vec3 position;
} vs_out;

void main() {

    gl_Position = pr_matrix * vw_matrix * ml_matrix * position;

    vs_out.tc = tc;

    if (top == 1)
        vs_out.tc.y = 1.0 - vs_out.tc.y;
    vs_out.position = vec3(vw_matrix * position);
}