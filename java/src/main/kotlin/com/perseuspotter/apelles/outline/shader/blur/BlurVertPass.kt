package com.perseuspotter.apelles.outline.shader.blur

import com.perseuspotter.apelles.depression.Shader

object BlurVertPass : Shader(getResource("/shaders/blur/blurVertPass.frag"), getResource("/shaders/blur/blurVertPass.vert"))