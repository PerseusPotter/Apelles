#version 120
// taken from SBA

varying vec2 outTextureCoords;
varying vec3 outPosition;
varying vec4 outColor;

void main() {
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

    outTextureCoords = gl_MultiTexCoord0.st;
    outPosition = gl_Vertex.xyz;
    outColor = gl_Color;
}