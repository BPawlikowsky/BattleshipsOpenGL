#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 ml_matrix;
uniform mat4 vw_matrix = mat4(1.0);


out DATA
{
    vec2 tc;
} vs_out;

void main() {
    gl_Position = pr_matrix * vw_matrix  * ml_matrix * vec4(position, 1.0);
    vs_out.tc = tc;
}