#version 120

uniform sampler2D outTexture;
uniform vec4 color;

varying vec2 outTextureCoords;

void main() {
  vec4 col = texture2D(outTexture, outTextureCoords);
  if (col.a < 0.1) discard;

  gl_FragColor = vec4(color.rgb, color.a * col.a);
}