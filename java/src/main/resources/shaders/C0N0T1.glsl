#version 310 core

layout(location=0)in vec3 position;
layout(location=3)in vec2 texCoord;

out vec2 fragTexCoord

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(){
    gl_Position=projection*view*model*vec4(position,1);
    fragTexCoord=texCoord;
}
