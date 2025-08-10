package com.perseuspotter.apelles.geo.dim3.stair.straight

import com.perseuspotter.apelles.geo.Point
import com.perseuspotter.apelles.geo.dim3.stair.Orientation

object OrientationStraight : Orientation(arrayOf(
    // 0
    Point(0.0 - ep, 0.0 - ep, 0.0 - ep),
    Point(0.0 - ep, 0.0 - ep, 1.0 + ep),
    Point(1.0 + ep, 0.0 - ep, 0.0 - ep),
    Point(1.0 + ep, 0.0 - ep, 1.0 + ep),
    // 4
    Point(0.0 - ep, 0.5 + ep, 0.0 - ep),
    Point(0.0 - ep, 0.5 + ep, 1.0 + ep),
    // 6
    Point(0.5 - ep, 0.5 + ep, 0.0 - ep),
    Point(0.5 - ep, 0.5 + ep, 1.0 + ep),
    // 8
    Point(0.5 - ep, 1.0 + ep, 0.0 - ep),
    Point(0.5 - ep, 1.0 + ep, 1.0 + ep),
    // 10
    Point(1.0 + ep, 1.0 + ep, 0.0 - ep),
    Point(1.0 + ep, 1.0 + ep, 1.0 + ep)
))