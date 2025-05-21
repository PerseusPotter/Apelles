package com.perseuspotter.apelles.outline.shader.sobel

import com.perseuspotter.apelles.depression.ChromaShader

object SobelRender : ChromaShader(getResource("/shaders/sobel/sobelRender.frag"), getResource("/shaders/sobel/sobelRender.vert"), true)