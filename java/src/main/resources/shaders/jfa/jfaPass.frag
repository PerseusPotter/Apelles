#version 130

uniform int outlineGap;
uniform sampler2D pingPong;
uniform ivec2 dim;

out vec4 fragColor;

void main() {
  ivec2 pos = ivec2(gl_FragCoord.xy);
  vec4 data = texelFetch(pingPong, pos, 0);
  float minDist = (data.w == 0.0 ? -1.0 : data.x);
  if (minDist == 0.0) fragColor = data;
  else {
    vec3 best = data.yzw;

    for (int u = -1; u <= 1; u++) {
      for (int v = -1; v <= 1; v++) {
        if (u == 0 && v == 0) continue;

        ivec2 samplePos = ivec2(u, v) * outlineGap + pos;
        samplePos = clamp(samplePos, ivec2(0, 0), dim - 1);

        vec4 sample = texelFetch(pingPong, samplePos, 0);
        if (sample.w == 0.0) continue;
        float w = 1.0 / fract(sample.w);
        if (w < outlineGap) continue;

        vec2 dPos = sample.yz - gl_FragCoord.xy;
        float dist = dot(dPos, dPos);

        if (minDist < 0.0 || dist < minDist) {
          minDist = dist;
          best = sample.yzw;
        }
      }
    }

    if (minDist < 0.0) fragColor = data;
    else fragColor = vec4(minDist, best);
  }
}