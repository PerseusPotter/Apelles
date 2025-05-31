package com.perseuspotter.apelles.geo.dim3.stair.inner

import com.perseuspotter.apelles.geo.Point
import com.perseuspotter.apelles.geo.dim3.stair.Orientation

object OrientationInner : Orientation(arrayOf(
    Point(0.75, 0.25, 0.75),
    // 0
    Point(0.0 - ep, 0.0 - ep, 0.0 - ep),
    Point(0.0 - ep, 0.0 - ep, 1.0 + ep),
    Point(1.0 + ep, 0.0 - ep, 0.0 - ep),
    Point(1.0 + ep, 0.0 - ep, 1.0 + ep),
    // 4
    Point(0.0 - ep, 0.5 + ep, 0.0 - ep),
    Point(0.0 - ep, 0.5 + ep, 0.5 - ep),
    Point(0.5 - ep, 0.5 + ep, 0.0 - ep),
    Point(0.5 - ep, 0.5 + ep, 0.5 - ep),
    // 8
    Point(0.0 - ep, 1.0 + ep, 0.5 - ep),
    Point(0.0 - ep, 1.0 + ep, 1.0 + ep),
    Point(0.5 - ep, 1.0 + ep, 0.0 - ep),
    Point(0.5 - ep, 1.0 + ep, 0.5 - ep),
    Point(1.0 + ep, 1.0 + ep, 0.0 - ep),
    Point(1.0 + ep, 1.0 + ep, 1.0 + ep)
))