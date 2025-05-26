#version 140

uniform sampler2D sampler;

out vec4 fragColor;

/*
// σ = 3, r = 5
const s = 3;
const r = 5;
const k = Array.from({ length: r + 1 }, (_, i) => Math.exp(-(i * i) / (2 * s * s)));
const t = k.reduce((a, v) => a + v, 0);
console.log(k.map(v => v / t).map((v, i) => `#define K${i} ${v.toFixed(10)}`).join('\n'));
console.log(Array.from({ length: r }, (_, i) => `vec4 kl${r - i} = texelFetch(sampler, pos + ivec2(-${r - i}, +0), 0);`).join('\n'));
console.log('vec4 k0 = texelFetch(sampler, pos + ivec2(+0, +0), 0);');
console.log(Array.from({ length: r }, (_, i) => `vec4 kr${i + 1} = texelFetch(sampler, pos + ivec2(+${i + 1}, +0), 0);`).join('\n'));
console.log(`fragColor = ${Array.from({ length: r }, (_, i) => `(kl${r - i} + kr${r - i}) * K${r - i}`).join(' + ')} + k0 * K0;`);
*/
#define K0 0.2491471673
#define K1 0.2356831221
#define K2 0.1995014557
#define K3 0.1511153958
#define K4 0.1024274626
#define K5 0.0621253965

void main(void) {
  ivec2 pos = ivec2(gl_FragCoord.xy);

  vec4 kl5 = texelFetch(sampler, pos + ivec2(-5, +0), 0);
  vec4 kl4 = texelFetch(sampler, pos + ivec2(-4, +0), 0);
  vec4 kl3 = texelFetch(sampler, pos + ivec2(-3, +0), 0);
  vec4 kl2 = texelFetch(sampler, pos + ivec2(-2, +0), 0);
  vec4 kl1 = texelFetch(sampler, pos + ivec2(-1, +0), 0);
  vec4 k0 = texelFetch(sampler, pos + ivec2(+0, +0), 0);
  vec4 kr1 = texelFetch(sampler, pos + ivec2(+1, +0), 0);
  vec4 kr2 = texelFetch(sampler, pos + ivec2(+2, +0), 0);
  vec4 kr3 = texelFetch(sampler, pos + ivec2(+3, +0), 0);
  vec4 kr4 = texelFetch(sampler, pos + ivec2(+4, +0), 0);
  vec4 kr5 = texelFetch(sampler, pos + ivec2(+5, +0), 0);

  fragColor = (kl5 + kr5) * K5 + (kl4 + kr4) * K4 + (kl3 + kr3) * K3 + (kl2 + kr2) * K2 + (kl1 + kr1) * K1 + k0 * K0;
}