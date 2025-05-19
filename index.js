import { JavaTypeOrNull, throwExp } from './util';

const APRenderer = JavaTypeOrNull('com.perseuspotter.apelles.Renderer') ?? throwExp('jar not loaded correctly');
const APRendererI = APRenderer.INSTANCE;
export function ___________________________________shhh() {
  let t = 0;
  register('tick', () => t++);
  register('renderWorld', pt => {
    try {
      APRendererI.render(pt, t);
    } catch (e) {
      console.error(e);
    }
  }).setPriority(Priority.LOWEST);
}
const GlState = JavaTypeOrNull('com.perseuspotter.apelles.state.GlState')?.INSTANCE ?? throwExp('jar not loaded correctly');
const Frustum = JavaTypeOrNull('com.perseuspotter.apelles.geo.Frustum')?.INSTANCE ?? throwExp('jar not loaded correctly');
const Point = JavaTypeOrNull('com.perseuspotter.apelles.geo.Point') ?? throwExp('jar not loaded correctly');
const Geometry = JavaTypeOrNull('com.perseuspotter.apelles.geo.Geometry') ?? throwExp('jar not loaded correctly');
const GeometryC = Geometry.Companion;

/**
 * packed int is RGBA
 * @typedef {number | [number, number, number] | [number, number, number, number]} ColorLike
 */

/**
 * @typedef RenderOptions
 * @property {number} [lighting=0] `0` - 0 = none | 1 = smooth | 2 = flat
 * @property {boolean} [phase=false] `false`
 * @property {boolean} [cull=true] `true` - whether to frustum cull the object. you should only disable this if you know what you are doing. though i doubt there will be any false positives, the option is here
 * @property {number} [chroma=0] `0` - use chroma, the color will be interpreted as [chromaSize, speed, lightness & chroma, alpha] (hint, use `packChromaParams`). 0-no chroma | 1-2d chroma | 2-3d chroma
 */

// why was jsdoc being a bitch idk idc
/**
 * @typedef LineOptions
 * @property {number} [lw=1] `1` - width of stroke
 * @property {boolean} [smooth=false] `false` - whether to enable `GL_LINE_SMOOTH`
 */

// yea fuck this idc enough to make this "clean" typescript is not working with me
/**
 * @typedef AABBOptions
 * @property {number} [wz=w] `w`
 * @property {boolean} [centered=true] `true` - whether provided `<x, y, z>` coords specify the center of the box (y-axis is assumed to never be centered)
 */

// i love how typescript has so many open bug reports and feature requests that so many people want *cough*16665*cough* and yet they just never get implemented at least i got @overload that only took like 3 years to actually work
/**
 * @typedef BeaconOptions
 * @property {boolean} [centered=true] `true` - whether provided `<x, y, z>` coords specify the center of the beacon (y-axis is assumed to never be centered)
 * @property {number} [h=300-y] `300-y` - (default height extends beacon to y=300)
 */

/**
 * @typedef {RenderOptions & LineOptions} LineRenderOptions
 */

const ResourceLocation = JavaTypeOrNull('net.minecraft.util.ResourceLocation') ?? throwExp('failed to load minecraft???');
/**
 * @param {string | typeof ResourceLocation} rl
 */
function coerceResourceLocation(rl) {
  if (typeof rl === 'string') return new ResourceLocation(rl);
  return rl;
}

/**
 * @param {number} size [0, ∞)
 * @param {number} speed (-∞, ∞)
 * @param {number} lightness [0, 1]
 * @param {number} chroma [0, 0.4]
 * @param {number} [alpha=1] `1` [0, 1]
 * @returns {[number, number, number, number]}
 */
export function packChromaParams(size, speed, lightness, chroma, alpha = 1) {
  return [1 / size, speed, Math.floor(256 * lightness) + chroma / 256, alpha];
}

const addPrimitive = APRendererI.addPrimitive ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {[number, number, number][]} points
 * @param {LineRenderOptions} options
 */
export function renderLine(color, points, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addPrimitive.call(APRendererI, color, 3, points, lw, lighting, phase, smooth, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} mode GL primitive constant
 * @param {[number, number, number][]} points
 * @param {LineRenderOptions} options
 */
export function renderPrimitive(color, mode, points, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addPrimitive.call(APRendererI, color, mode, points, lw, lighting, phase, smooth, cull, chroma);
}

const addBoxO = APRendererI.addBoxO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {LineRenderOptions & AABBOptions} options
 */
export function renderBoxOutline(color, x, y, z, w, h, { centered = true, wz = w, lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addBoxO.call(APRendererI, color, x, y, z, w, h, wz, centered, lw, lighting, phase, smooth, cull, chroma);
}

const addAABBO = APRendererI.addAABBO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x1
 * @param {number} y1
 * @param {number} z1
 * @param {number} x2
 * @param {number} y2
 * @param {number} z2
 * @param {LineRenderOptions} options
 */
export function renderAABBOutline(color, x1, y1, z1, x2, y2, z2, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addAABBO.call(APRendererI,
    color,
    Math.min(x1, x2),
    Math.min(y1, y2),
    Math.min(z1, z2),
    Math.max(x1, x2),
    Math.max(y1, y2),
    Math.max(z1, z2),
    lw,
    lighting,
    phase,
    smooth,
    cull,
    chroma
  );
}
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param aabb net.minecraft.util.AxisAlignedBB
 * @param {RenderOptions} options
 */
export function renderMCAABBOutline(color, aabb, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addAABBO.call(APRendererI,
    color,
    aabb.field_72340_a,
    aabb.field_72338_b,
    aabb.field_72339_c,
    aabb.field_72336_d,
    aabb.field_72337_e,
    aabb.field_72334_f,
    lw,
    lighting,
    phase,
    smooth,
    cull,
    chroma
  );
}

const addBoxF = APRendererI.addBoxF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {RenderOptions & AABBOptions} options
 */
export function renderBoxFilled(color, x, y, z, w, h, { centered = true, wz = w, lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addBoxF.call(APRendererI, color, x, y, z, w, h, wz, centered, lighting, phase, cull, chroma);
}

const addAABBF = APRendererI.addAABBF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x1
 * @param {number} y1
 * @param {number} z1
 * @param {number} x2
 * @param {number} y2
 * @param {number} z2
 * @param {RenderOptions} options
 */
export function renderAABBFilled(color, x1, y1, z1, x2, y2, z2, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addAABBF.call(APRendererI,
    color,
    Math.min(x1, x2),
    Math.min(y1, y2),
    Math.min(z1, z2),
    Math.max(x1, x2),
    Math.max(y1, y2),
    Math.max(z1, z2),
    lighting,
    phase,
    cull,
    chroma
  );
}
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param aabb net.minecraft.util.AxisAlignedBB
 * @param {RenderOptions} options
 */
export function renderMCAABBFilled(color, aabb, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addAABBF.call(APRendererI,
    color,
    aabb.field_72340_a,
    aabb.field_72338_b,
    aabb.field_72339_c,
    aabb.field_72336_d,
    aabb.field_72337_e,
    aabb.field_72334_f,
    lighting,
    phase,
    cull,
    chroma
  );
}

const addBeacon = APRendererI.addBeacon ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {RenderOptions & BeaconOptions} options
 */
export function renderBeacon(color, x, y, z, { centered = true, h = 300 - y, lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  if (!centered) {
    x += 0.5;
    z += 0.5;
  }
  addBeacon.call(APRendererI, color, x, y, z, h, lighting, phase, cull, chroma);
}

const addCircle = APRendererI.addCircle ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} r
 * @param {number} segments
 * @param {LineRenderOptions} options
 */
export function renderCircle(color, x, y, z, r, segments, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addCircle.call(APRendererI, color, x, y, z, r, segments, lw, lighting, phase, smooth, cull, chroma);
}

const addIcosphere = APRendererI.addIcosphere ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} r
 * @param {number} divisions - warning, scales exponentially (0 = 20 triangles, 1 = 80, 2 = 320, 3 = 1280)
 * @param {RenderOptions} options
 */
export function renderSphere(color, x, y, z, r, divisions, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addIcosphere.call(APRendererI, color, x, y, z, r, divisions, lighting, phase, cull, chroma);
}

const addPyramidO = APRendererI.addPyramidO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} r length from center of base to corner, equal to `apothem / cos(PI / n)`
 * @param {number} h hint: negative to flip upside down
 * @param {number} n number of sides on base
 * @param {LineRenderOptions} options
 */
export function renderPyramidOutline(color, x, y, z, r, h, n, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addPyramidO.call(APRendererI, color, x, y, z, r, h, n, lw, lighting, phase, smooth, cull, chroma);
}

const addPyramidF = APRendererI.addPyramidF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} r length from center to corner, equal to `apothem / cos(PI / n)`
 * @param {number} h hint: negative to flip upside down
 * @param {number} n number of sides on base
 * @param {RenderOptions} options
 */
export function renderPyramidFilled(color, x, y, z, r, h, n, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addPyramidF.call(APRendererI, color, x, y, z, r, h, n, lighting, phase, cull, chroma);
}

const addVertCylinder = APRendererI.addVertCylinder ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} r
 * @param {number} h
 * @param {number} segments
 * @param {RenderOptions} options
 */
export function renderVerticalCylinder(color, x, y, z, r, h, segments, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addVertCylinder.call(APRendererI, color, x, y, z, r, h, segments, lighting, phase, cull, chroma);
}

const addOctahedronO = APRendererI.addOctahedronO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {LineRenderOptions} options
 */
export function renderOctahedronOutline(color, x, y, z, w, h, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addOctahedronO.call(APRendererI, color, x, y, z, w / 2, h / 2, lw, lighting, phase, smooth, cull, chroma);
}

const addOctahedronF = APRendererI.addOctahedronF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {RenderOptions} options
 */
export function renderOctahedronFilled(color, x, y, z, w, h, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addOctahedronF.call(APRendererI, color, x, y, z, w / 2, h / 2, lighting, phase, cull, chroma);
}

const addStraightStairO = APRendererI.addStraightStairO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {LineRenderOptions} options
 */
export function renderStraightStairOutline(color, x, y, z, type, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addStraightStairO.call(APRendererI, color, x, y, z, type, lw, lighting, phase, smooth, cull, chroma);
}

const addStraightStairF = APRendererI.addStraightStairF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {RenderOptions} options
 */
export function renderStraightStairFilled(color, x, y, z, type, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addStraightStairF.call(APRendererI, color, x, y, z, type, lighting, phase, cull, chroma);
}

const addInnerStairO = APRendererI.addInnerStairO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {boolean} left is the stair shape `INNER_LEFT`
 * @param {LineRenderOptions} options
 */
export function renderInnerStairOutline(color, x, y, z, type, left, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addInnerStairO.call(APRendererI, color, x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma);
}

const addInnerStairF = APRendererI.addInnerStairF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {boolean} left is the stair shape `INNER_LEFT`
 * @param {RenderOptions} options
 */
export function renderInnerStairFilled(color, x, y, z, type, left, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  (typeof color === 'number' ? addInnerStairFN : addInnerStairFA).call(APRendererI, color, x, y, z, type, left, lighting, phase, cull, chroma);
}

const addOuterStairO = APRendererI.addOuterStairO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {boolean} left is the stair shape `OUTER_LEFT`
 * @param {LineRenderOptions} options
 */
export function renderOuterStairOutline(color, x, y, z, type, left, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addOuterStairO.call(APRendererI, color, x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma);
}

const addOuterStairF = APRendererI.addOuterStairF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {boolean} left is the stair shape `OUTER_LEFT`
 * @param {RenderOptions} options
 */
export function renderOuterStairFilled(color, x, y, z, type, left, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addOuterStairF.call(APRendererI, color, x, y, z, type, left, lighting, phase, cull, chroma);
}

const addStairO = APRendererI.addStairO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {LineRenderOptions} options
 */
export function renderStairOutline(color, x, y, z, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addStairO.call(APRendererI, color, x, y, z, lw, lighting, phase, smooth, cull, chroma);
}

const addStairF = APRendererI.addStairF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {RenderOptions} options
 */
export function renderStairFilled(color, x, y, z, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addStairF.call(APRendererI, color, x, y, z, lighting, phase, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param bp net.minecraft.util.BlockPos
 * @param {LineRenderOptions} options
 */
export function renderStairOutlineBP(color, bp, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addStairO.call(APRendererI, color, bp, lw, lighting, phase, smooth, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param bp net.minecraft.util.BlockPos
 * @param {RenderOptions} options
 */
export function renderStairFilledBP(color, bp, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addStairF.call(APRendererI, color, bp, lighting, phase, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param bs net.minecraft.block.state.IBlockState
 * @param {LineRenderOptions} options
 */
export function renderStairOutlineBS(color, x, y, z, bs, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addStairO.call(APRendererI, color, x, y, z, bs, lw, lighting, phase, smooth, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param bs net.minecraft.block.state.IBlockState
 * @param {RenderOptions} options
 */
export function renderStairFilledBS(color, x, y, z, bs, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addStairF.call(APRendererI, color, x, y, z, bs, lighting, phase, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type MSB [set if not straight] | [set if outer] | [set if left] | [rest of metadata (length 3)] LSB
 * @param {LineRenderOptions} options
 */
export function renderStairOutlineManual(color, x, y, z, type, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  addStairO.call(APRendererI, color, x, y, z, type, lw, lighting, phase, smooth, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type MSB [set if not straight] | [set if outer] | [set if left] | [rest of metadata (length 3)] LSB
 * @param {RenderOptions} options
 */
export function renderStairFilledManual(color, x, y, z, type, { lighting = 0, phase = false, cull = true, chroma = 0 } = {}) {
  addStairF.call(APRendererI, color, x, y, z, type, lighting, phase, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {LineRenderOptions} options
 */
export function renderTracer(color, x, y, z, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  const p = Player.getPlayer();
  if (!p) return;
  const look = p.func_70676_i(Tessellator.partialTicks);
  renderLine(color, [
    [x, y, z],
    [
      getRenderX() + look.field_72450_a,
      getRenderY() + look.field_72448_b + p.func_70047_e(),
      getRenderZ() + look.field_72449_c
    ]
  ], { lw, lighting, phase, smooth, cull, chroma });
}

const _lineWidth = GlState['lineWidth(float)'] ?? throwExp('bad');
/**
 * @param {number} lw
 */
export function lineWidth(lw) {
  _lineWidth.call(GlState, lw);
}

const _lineSmooth = GlState['lineSmooth(boolean)'] ?? throwExp('bad');
/**
 * @param {boolean} enabled
 */
export function lineSmooth(enabled) {
  _lineSmooth.call(GlState, enabled);
}

const _bindTexture = GlState['bindTexture(net.minecraft.util.ResourceLocation)'] ?? throwExp('bad');
/**
 * @param {string} tex `ResourceLocation` or texture path, e.g. `'textures/entity/beacon_beam.png'`
 */
export function bindTexture(tex) {
  _bindTexture.call(GlState, coerceResourceLocation(tex));
}

const _color = GlState['color(float,float,float,float)'] ?? throwExp('bad');
/**
 * @param {number} r [0, 1]
 * @param {number} g [0, 1]
 * @param {number} b [0, 1]
 * @param {number} a [0, 1]
 */
export function color(r, g, b, a) {
  _color.call(GlState, r, g, b, a);
}

const _setDepthTest = GlState['setDepthTest(boolean)'] ?? throwExp('bad');
/**
 * @param {boolean} enabled
 */
export function depthTest(enabled) {
  _setDepthTest.call(GlState, enabled);
}

const _setLighting = GlState['setLighting(int)'] ?? throwExp('bad');
/**
 * @param {number} mode 0 = none | 1 = smooth | 2 = flat
 */
export function lighting(mode) {
  _setLighting.call(GlState, mode);
}

const _test = Frustum['test(double,double,double)'] ?? throwExp('bad');
/**
 * frustum check, not hitscan
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @returns {boolean}
 */
export function isInView(x, y, z) {
  return _test.call(Frustum, x, y, z);
}

const _clip = Frustum['clip(com.perseuspotter.apelles.geo.Point,com.perseuspotter.apelles.geo.Point)'] ?? throwExp('bad');
/**
 * clips line segment to frustum
 * @param {number} x1
 * @param {number} y1
 * @param {number} z1
 * @param {number} x2
 * @param {number} y2
 * @param {number} z2
 * @returns {[null, null] | [{ x: number, y: number, z: number}, { x: number, y: number, z: number }]}
 */
export function clipLine(x1, y1, z1, x2, y2, z2) {
  return _clip.call(Frustum, new Point(x1, y1, z1), new Point(x2, y2, z2));
}

const _getRenderX = GeometryC['getRenderX()'] ?? throwExp('bad');
const _getRenderY = GeometryC['getRenderY()'] ?? throwExp('bad');
const _getRenderZ = GeometryC['getRenderZ()'] ?? throwExp('bad');
/**
 * @returns {number}
 */
export function getRenderX() {
  return _getRenderX.call(GeometryC);
};
/**
 * @returns {number}
 */
export function getRenderY() {
  return _getRenderY.call(GeometryC);
};
/**
 * @returns {number}
 */
export function getRenderZ() {
  return _getRenderZ.call(GeometryC);
};

export const DefaultVertexFormats = JavaTypeOrNull('net.minecraft.client.renderer.vertex.DefaultVertexFormats') ?? throwExp('bad');
const worldRen = Geometry.worldRen;
const _func_181668_a = worldRen['func_181668_a(int,net.minecraft.client.renderer.vertex.VertexFormat)'] ?? throwExp('bad');
/**
 * @param {number} mode
 */
export function begin(mode, format = DefaultVertexFormats.field_181705_e) {
  _func_181668_a.call(worldRen, mode, format);
}
const _func_181662_b = worldRen['func_181662_b(double,double,double)'] ?? throwExp('bad');
const _func_181673_a = worldRen['func_181673_a(double,double)'] ?? throwExp('bad');
const _func_181675_d = worldRen['func_181675_d()'] ?? throwExp('bad');
/**
 * @overload
 * @param {number} x
 * @param {number} y
 * @param {number} z
 *
 * @overload
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} u
 * @param {number} v
 */
export function pos(x, y, z, u, v) {
  _func_181662_b.call(worldRen, x, y, z);
  if (u !== undefined) _func_181673_a.call(worldRen, u, v);
  _func_181675_d.call(worldRen);
}
const tess = Geometry.tess;
const _func_78381_a = tess['func_78381_a()'] ?? throwExp('bad');
export function draw() {
  _func_78381_a.call(tess);
}

const APColor = JavaTypeOrNull('com.perseuspotter.apelles.state.Color') ?? throwExp('jar not loaded correctly');
/**
 * @param {ColorLike} color
 * @returns {typeof APColor}
 */
function coerceColor(color) {
  if (typeof color === 'number') return new APColor(
    (color >>> 24) / 255,
    ((color >> 16) & 0xFF) / 255,
    ((color >> 8) & 0xFF) / 255,
    (color & 0xFF) / 255
  );
  return new APColor(color[0], color[1], color[2], color[3] ?? 1);
}

/**
 * @typedef MCEntity
 */

const outliners = [];
register('gameUnload', () => outliners.forEach(v => v.unregister()));

/**
 * @typedef OutlineRenderOptions
 * @property {boolean} [phase=false] `false`
 * @property {boolean} [chroma=false] `false` - use chroma, the color will be interpreted as [chromaSize, speed, saturation, alpha] all with values ranging from [0, 1] (except you can go outside and it should work). only 2d chroma :(
 * @property {boolean} [blackOutline=true] `true` - outermost pixel is a black outline
 * @property {boolean} [absoluteSize=true] `true` - if true pixel scales with size in world (distance)
 */

const ManualOutliner = JavaTypeOrNull('com.perseuspotter.apelles.outline.outliner.ManualOutliner') ?? throwExp('jar not loaded correctly');
/**
 * INITIALLY UNREGISTERED
 *
 * must manually add entities to be outlined with `.add()`/`.remove()`
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} width int in pixels
 * @param {OutlineRenderOptions} options
 * @returns {{ setColor(color: number): void, setColor(color: [number, number, number] | [number, number, number, number]): void, add(ent: MCEntity): void, remove(ent: MCEntity): void, clear(): void, register(): void, unregister(): void }}
 */
export function createManualOutliner(color, width, { phase = false, chroma = false, blackOutline = true, absoluteSize = true } = {}) {
  const o = new ManualOutliner(coerceColor(color), width, phase, chroma, blackOutline, absoluteSize);
  outliners.push(o);
  return o;
}

const OutlineTester = JavaTypeOrNull('com.perseuspotter.apelles.outline.OutlineTester') ?? throwExp('jar not loaded correctly');
/**
 * @typedef MCEntityClass
 */
/**
 * @typedef OutlineTester
 * @property {(clazz: MCEntityClass) => boolean} addWhitelist
 * @property {(clazz: MCEntityClass) => boolean} addBlacklist
 * @property {(clazz: MCEntityClass) => boolean} removeWhitelist
 * @property {(clazz: MCEntityClass) => boolean} removeBlacklist
 * @property {(ent: MCEntity) => boolean} test you should not need to call this
 * @property {(ent: MCEntity) => boolean} shouldOutline you should not need to call this
 */
/**
 * @param {(ent: MCEntity) => boolean} func
 * @returns {OutlineTester}
 */
export function createCustomOutlineTester(func) {
  return new OutlineTester.Custom(func);
}
/**
 * equivalent to `createCustomOutlineTester(() => true);`
 * @returns {OutlineTester}
 */
export function createPassthroughOutlineTester() {
  return new OutlineTester.Always();
}

const PerEntityOutliner = JavaTypeOrNull('com.perseuspotter.apelles.outline.outliner.PerEntityOutliner') ?? throwExp('jar not loaded correctly');
/**
 * INITIALLY UNREGISTERED
 *
 * will outline entities that pass the `tester`, only tested once per entity (the first attempt at outlining it)
 * @param {OutlineTester} tester
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} width int in pixels
 * @param {OutlineRenderOptions} options
 * @returns {{ setColor(color: number): void, setColor(color: [number, number, number] | [number, number, number, number]): void, register(): void, unregister(): void, clear(): void }}
 */
export function createPerEntityOutliner(tester, color, width, { phase = false, chroma = false, blackOutline = true, absoluteSize = true } = {}) {
  const o = new PerEntityOutliner(tester, coerceColor(color), width, phase, chroma, blackOutline, absoluteSize);
  outliners.push(o);
  return o;
}

const PerFrameOutliner = JavaTypeOrNull('com.perseuspotter.apelles.outline.outliner.PerFrameOutliner') ?? throwExp('jar not loaded correctly');
/**
 * INITIALLY UNREGISTERED
 *
 * will outline entities that pass the `tester`, retested every frame
 * @param {OutlineTester} tester
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} width int in pixels
 * @param {OutlineRenderOptions} options
 * @returns {{ setColor(color: number): void, setColor(color: [number, number, number] | [number, number, number, number]): void, register(): void, unregister(): void }}
 */
export function createPerFrameOutliner(tester, color, width, { phase = false, chroma = false, blackOutline = true, absoluteSize = true } = {}) {
  const o = new PerFrameOutliner(tester, coerceColor(color), width, phase, chroma, blackOutline, absoluteSize);
  outliners.push(o);
  return o;
}

const SemiAutomaticOutliner = JavaTypeOrNull('com.perseuspotter.apelles.outline.outliner.SemiAutomaticOutliner') ?? throwExp('jar not loaded correctly');
/**
 * INITIALLY UNREGISTERED
 *
 * will outline entities that pass the `tester`, only tested once per entity (the first attempt at outlining it), except can manually modify the internal state. unless you know what you are doing you should not be using this.
 * @param {OutlineTester} tester
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} width int in pixels
 * @param {OutlineRenderOptions} options
 * @returns {{ setColor(color: number): void, setColor(color: [number, number, number] | [number, number, number, number]): void, register(): void, unregister(): void, clear(): void, add(ent: MCEntity): void, remove(ent: MCEntity): void, retest(ent: MCEntity): void }}
 */
export function createSemiAutomaticOutliner(tester, color, width, { phase = false, chroma = false, blackOutline = true, absoluteSize = true } = {}) {
  const o = new SemiAutomaticOutliner(tester, coerceColor(color), width, phase, chroma, blackOutline, absoluteSize);
  outliners.push(o);
  return o;
}

// require('./test');

register('command', () => Client.scheduleTask(20, () => Java.type('com.perseuspotter.apelles.outline.EntityOutlineRenderer').dump = true)).setName('apellesdumpfbo', true);
register('command', t => Client.scheduleTask(() => APRenderer.USE_NEW_SHIT = !!t)).setName('apellesusecompat', true);