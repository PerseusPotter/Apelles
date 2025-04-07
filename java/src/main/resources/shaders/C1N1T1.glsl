#version 310 core

layout(location=0)in vec3 position;
layout(location=1)in vec4 color;
layout(location=2)in vec3 normal;
layout(location=3)in vec2 texCoord;

out vec4 fragColor;
out vec2 fragTexCoord
out vec3 fragNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(){
    gl_Position=projection*view*model*vec4(position,1);
    fragColor=color;
    fragTexCoord=texCoord;
    fragNormal=normal;
}
