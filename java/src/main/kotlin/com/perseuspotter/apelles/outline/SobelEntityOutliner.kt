package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.outline.shader.sobel.SobelRender

object SobelEntityOutliner : KernelEntityOutliner(3, "Sobel", SobelRender)