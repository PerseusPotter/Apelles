#version 120
// taken from SBA

uniform float timeOffset;

varying vec3 outPosition;
varying vec4 outColor;

vec3 hsb2rgb_smooth(vec3 c) {
    vec3 rgb = clamp(abs(mod(c.x * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0, 0.0, 1.0);
	rgb = rgb * rgb * (3.0 - 2.0 * rgb); // Cubic smoothing
	return c.z * mix(vec3(1.0), rgb, c.y);
}

void main() {
    // The hue takes in account the position, chroma settings, and time
	float hue = mod(((outPosition.x - outPosition.y + outPosition.z) / outColor.r) - timeOffset * outColor.g, 1.0);

	// Set the color to use the new hue & chroma settings
	gl_FragColor = vec4(hsb2rgb_smooth(vec3(hue, outColor.b, 1.0)), outColor.a);
}