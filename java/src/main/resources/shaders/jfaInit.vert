#version 120

uniform int outlineWidth;
uniform vec2 dim;

varying vec2 outTextureCoords;
varying float actualWidth;

void main() {
  vec4 pos = gl_ModelViewProjectionMatrix * gl_Vertex;
  gl_Position = vec4(pos.xy, pos.z - 0.00001, pos.w);
  outTextureCoords = gl_MultiTexCoord0.st;
  if (outlineWidth >= 0) actualWidth = outlineWidth;
  else {
    vec4 pos2 = gl_ModelViewProjectionMatrix * vec4(gl_Vertex.xyz / gl_Vertex.w + normalize(vec3(gl_ModelViewMatrix[0][0], gl_ModelViewMatrix[1][0], gl_ModelViewMatrix[2][0])) * 0.0125, 1.0);
    vec2 ndcDelta = (pos2.xy / pos2.w) - (pos.xy / pos.w);
    vec2 pixelDelta = ndcDelta * dim * 0.5;
    actualWidth = -outlineWidth * length(pixelDelta);
  }
}