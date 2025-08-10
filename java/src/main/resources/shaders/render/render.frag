#version 330 core

  #ifdef LIGHTING_BLINN_PHONG
in vec3 Pos;
in vec3 Normal;
  #elif defined(LIGHTING_FLAT)
flat in vec3 Pos;
flat in vec3 Normal;
  #endif
in vec4 Color;
  #ifdef TEXTURED
in vec2 TexUV;
  #endif
  #ifdef CHROMA_3D
in vec3 VertPos;
  #endif

out vec4 FragColor;

  #ifdef TEXTURED
uniform sampler2D uTex;
  #endif
  #ifdef ALPHA_TEST
uniform float uAlphaThresh;
  #endif

  #ifdef CHROMA
uniform vec3 uViewPos;
uniform float uTimeOffset;
    #ifdef CHROMA_2D
uniform float uOneOverDisplayWidth;
    #endif

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
  #endif

  #ifdef LIGHTING
struct DirectionalLight {
  vec4 direction;
  vec4 ambient;
  vec4 diffuse;
  vec4 specular;
};

#define LIGHT_COUNT 5
layout(std140) uniform lightsUbo {
  DirectionalLight lights[LIGHT_COUNT];
};

const vec3 sceneAmbient = vec3(0.4);
  #endif

void main()
{
  FragColor = Color;
    #ifdef TEXTURED
  vec4 orig = texture(uTex, TexUV);
  FragColor.a *= orig.a;
    #endif

    #ifdef ALPHA_TEST
  if (FragColor.a < uAlphaThresh) discard;
    #endif

    #ifdef CHROMA
      #ifdef CHROMA_2D
	float hue = ((gl_FragCoord.x - gl_FragCoord.y) * uOneOverDisplayWidth * Color.r) - uTimeOffset * Color.g;
      #elif defined(CHROMA_3D)
  float hue = ((VertPos.x + uViewPos.x - VertPos.y - uViewPos.y + VertPos.z + uViewPos.z) * Color.r) - uTimeOffset * Color.g;
      #endif
	float lightness = floor(Color.b) * ONE_OVER_256;
	float chroma = fract(Color.b) * 256.0;
      #ifdef TEXTURED
  lightness *= max(orig.r, max(orig.g, orig.b));
      #endif
  vec3 Lab = vec3(lightness, chroma * cos(hue), chroma * sin(hue));
  vec3 lms = pow(M2_1 * Lab, vec3(3.0));
  FragColor.rgb = lmsToRgb * lms;
    #elif defined(TEXTURED)
  FragColor.rgb *= orig.rgb;
    #endif

    #ifdef LIGHTING
  vec3 result = sceneAmbient * FragColor.rgb;
  vec3 viewDir = normalize(-Pos);
  vec3 norm = normalize(Normal);

  for (int i = 0; i < LIGHT_COUNT; i++) {
    vec3 lightDir = -lights[i].direction.xyz;

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = lights[i].diffuse.xyz * diff;

    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(norm, halfwayDir), 0.0), 8.0);
    vec3 specular = lights[i].specular.xyz * spec;

    result += (lights[i].ambient.xyz + diffuse) * FragColor.rgb + specular;
  }

  FragColor.rgb = result;
    #endif
}