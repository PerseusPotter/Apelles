#version 140

uniform colorsUbo {
  vec4 colors[256];
};
uniform sampler2D pingPong;
// chroma taken from sba
uniform float timeOffset;
uniform float oneOverDisplayWidth;

out vec4 fragColor;

vec3 hsb2rgb_smooth(vec3 c) {
  vec3 rgb = clamp(abs(mod(c.x * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0, 0.0, 1.0);
	rgb = rgb * rgb * (3.0 - 2.0 * rgb); // Cubic smoothing
	return c.z * mix(vec3(1.0), rgb, c.y);
}

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
    float hue = mod(((gl_FragCoord.x - gl_FragCoord.y) * oneOverDisplayWidth / col.r) - timeOffset * col.g, 1.0);
    fragColor = vec4(hsb2rgb_smooth(vec3(hue, col.b, 1.0)), -col.a);
  }
}