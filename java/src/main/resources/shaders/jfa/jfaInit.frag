#version 120

uniform int colorId;
uniform sampler2D outTexture;

varying vec2 outTextureCoords;
varying float actualWidth;

void main() {
  vec4 col = texture2D(outTexture, outTextureCoords);
  if (col.a < 0.1) discard;
  gl_FragColor = vec4(0.0, gl_FragCoord.xy, 1.0 / actualWidth + colorId);
}