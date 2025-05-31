#version 120
// taken from SBA

varying vec3 outPosition;
varying vec4 outColor;

void main() {
  gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

  // Pass the position to the fragment shader
  outPosition = gl_Vertex.xyz;
  outColor = gl_Color;
}