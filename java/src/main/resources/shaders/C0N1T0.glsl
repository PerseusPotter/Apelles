#version 310 core

layout(location=0)in vec3 position;
layout(location=2)in vec3 normal;

out vec3 fragNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(){
    gl_Position=projection*view*model*vec4(position,1);
    fragNormal=normal;
}
