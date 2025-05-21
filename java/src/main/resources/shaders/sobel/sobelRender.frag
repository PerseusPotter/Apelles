#version 140

uniform sampler2D sampler;
uniform float timeOffset;
uniform float oneOverDisplayWidth;

out vec4 fragColor;

#define ONE_OVER_256 0.00390625
const mat3 M2_1 = mat3(
  1.0, 1.0, 1.0,
  0.3963377921737678, -0.1055613423236563, -0.0894841820949657,
  0.2158037580607587, -0.0638541747717058, -1.2914855378640917
);
const mat3 M1_1 = mat3(
  1.2270138511035211, -0.0405801784232805, -0.0763812845057069,
  -0.5577999806518222, 1.1122568696168375, -0.4214819784180126,
  0.2812561489664678, -0.0716766786656012, 1.5861632204407947
);
const mat3 M0_1 = mat3(
  3.2404542, -0.9692660, 0.0556434,
  -1.5371385, 1.8760108, -0.2040259,
  -0.4985314, 0.0415560, 1.0572252
);
const mat3 lmsToRgb = M0_1 * M1_1;

vec2 grayscale(vec4 col) {
  // return vec2(0.2126 * col.r + 0.7152 * col.g + 0.0722 * col.b, col.a);
  return vec2(0.333333 * col.r + 0.333333 * col.g + 0.333333 * col.b, col.a);
}

void main(void) {
  ivec2 pos = ivec2(gl_FragCoord.xy);
  vec2 k00 = grayscale(texelFetch(sampler, pos + ivec2(-1, -1), 0));
  vec2 k01 = grayscale(texelFetch(sampler, pos + ivec2(-1, +0), 0));
  vec2 k02 = grayscale(texelFetch(sampler, pos + ivec2(-1, +1), 0));
  vec2 k10 = grayscale(texelFetch(sampler, pos + ivec2(+0, -1), 0));
  vec4 k11 = texelFetch(sampler, pos + ivec2(+0, +0), 0);
  vec2 k12 = grayscale(texelFetch(sampler, pos + ivec2(+0, +1), 0));
  vec2 k20 = grayscale(texelFetch(sampler, pos + ivec2(+1, -1), 0));
  vec2 k21 = grayscale(texelFetch(sampler, pos + ivec2(+1, +0), 0));
  vec2 k22 = grayscale(texelFetch(sampler, pos + ivec2(+1, +1), 0));

  vec2 edgeH = k00 + 2.0 * k10 + k20 - (k02 + 2.0 * k12 + k22);
  vec2 edgeV = k00 + 2.0 * k01 + k02 - (k20 + 2.0 * k21 + k22);
  vec2 grad = sqrt(edgeH * edgeH + edgeV * edgeV);

  vec4 color = k11;
  if(color.a < 0.0) {
    float hue = ((gl_FragCoord.x - gl_FragCoord.y) * oneOverDisplayWidth * color.r) - timeOffset * color.g;
    float lightness = floor(color.b) * ONE_OVER_256;
    float chroma = fract(color.b) * 256.0;

    vec3 Lab = vec3(lightness, chroma * cos(hue), chroma * sin(hue));
    vec3 lms = pow(M2_1 * Lab, vec3(3.0));
    color = vec4(lmsToRgb * lms, -color.a);
  }
  fragColor = vec4(color.rgb, color.a * clamp(grad[0] + grad[1], 0.0, 1.0));
}