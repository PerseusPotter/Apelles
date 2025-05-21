package com.perseuspotter.apelles.outline.shader.jfa

import com.perseuspotter.apelles.outline.shader.UBOColorShader

object JFARender : UBOColorShader(getResource("/shaders/jfa/jfaRender.frag"), getResource("/shaders/jfa/jfaRender.vert"))