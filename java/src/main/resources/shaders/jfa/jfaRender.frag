#version 140

uniform colorsUbo {
  vec4 colors[256];
};
uniform sampler2D pingPong;
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

void main() {
  vec4 data = texelFetch(pingPong, ivec2(gl_FragCoord.xy), 0);
  // if (data.w == 0.0) discard;
  if (data.x == 0.0) discard;
  float w = 1.0 / fract(data.w);
  if (w * w < data.x) discard;
  float d = w - sqrt(data.x);
  vec4 col = colors[int(data.w)];
  if (col.b < 0.0) {
    if (d < w * 0.25) {
      col = vec4(0.0, 0.0, 0.0, abs(col.a));
    } else col.b *= -1.0;
  }
  if (col.a > 0) fragColor = col;
  else {
    float hue = ((gl_FragCoord.x - gl_FragCoord.y) * oneOverDisplayWidth * col.r) - timeOffset * col.g;
    float lightness = floor(col.b) * ONE_OVER_256;
    float chroma = fract(col.b) * 256.0;

    vec3 Lab = vec3(lightness, chroma * cos(hue), chroma * sin(hue));
    vec3 lms = pow(M2_1 * Lab, vec3(3.0));
    fragColor = vec4(lmsToRgb * lms, -col.a);
  }
}