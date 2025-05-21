#version 120

varying vec2 outTextureCoords;

void main() {
  gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

  outTextureCoords = gl_MultiTexCoord0.st;
}