#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec4 aColor;
  #ifdef LIGHTING
layout(location = 2) in vec3 aNormal;
    #ifdef TEXTURED
layout(location = 3) in vec2 aTexUV;
    #endif
  #elif defined(TEXTURED)
layout(location = 2) in vec2 aTexUV;
  #endif

  #ifdef LIGHTING
uniform mat4 uModelView;
uniform mat4 uProj;
uniform mat3 uNorm;
  #else
uniform mat4 uModelViewProj;
  #endif

  #ifdef LIGHTING_BLINN_PHONG
out vec3 Pos;
out vec3 Normal;
  #elif defined(LIGHTING_FLAT)
flat out vec3 Pos;
flat out vec3 Normal;
  #endif
out vec4 Color;
  #ifdef TEXTURED
out vec2 TexUV;
  #endif
  #ifdef CHROMA_3D
out vec3 VertPos;
  #endif

void main() {
    #ifdef LIGHTING
  vec4 viewPos = uModelView * vec4(aPos, 1.0);
  Pos = viewPos.xyz;
  Normal = normalize(uNorm * aNormal);
    #endif
  Color = aColor;
    #ifdef TEXTURED
  TexUV = aTexUV;
    #endif
    #ifdef CHROMA_3D
  VertPos = aPos;
    #endif

    #ifdef LIGHTING
  gl_Position = uProj * viewPos;
    #else
  gl_Position = uModelViewProj * vec4(aPos, 1.0);
    #endif
}