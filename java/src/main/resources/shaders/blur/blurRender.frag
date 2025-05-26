#version 140

uniform sampler2D sampler;

out vec4 fragColor;

void main(void) {
  fragColor = texelFetch(sampler, ivec2(gl_FragCoord.xy), 0);
}