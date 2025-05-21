package com.perseuspotter.apelles.outline.shader.jfa

import com.perseuspotter.apelles.outline.shader.UBOColorRender

object JFARender : UBOColorRender(getResource("/shaders/jfaRender.frag"), getResource("/shaders/jfaRender.vert"))