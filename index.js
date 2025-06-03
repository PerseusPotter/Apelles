import { JavaTypeOrNull, throwExp } from './util';

const APRenderer = JavaTypeOrNull('com.perseuspotter.apelles.Renderer') ?? throwExp('jar not loaded correctly');
const APRendererI = APRenderer.INSTANCE;
export function ___________________________________shhh() {
  let t = 0;
  register('tick', () => t++);
  register('renderWorld', pt => {
    uploadBatched();
    APRendererI.render(pt, t);
  }).setPriority(Priority.LOWEST);
}
const GlState = JavaTypeOrNull('com.perseuspotter.apelles.state.GlState')?.INSTANCE ?? throwExp('jar not loaded correctly');
const Frustum = JavaTypeOrNull('com.perseuspotter.apelles.geo.Frustum')?.INSTANCE ?? throwExp('jar not loaded correctly');
const Point = JavaTypeOrNull('com.perseuspotter.apelles.geo.Point') ?? throwExp('jar not loaded correctly');
const Geometry = JavaTypeOrNull('com.perseuspotter.apelles.geo.Geometry') ?? throwExp('jar not loaded correctly');
const GeometryC = Geometry.Companion;
const BatchUploader = JavaTypeOrNull('com.perseuspotter.apelles.BatchUploader')?.INSTANCE ?? throwExp('jar not loaded correctly');
let batched = [];
let batchCalls = true;
function uploadBatched() {
  if (batched.length === 0) return;
  try {
    BatchUploader.upload(batched);
  } catch (e) {
    console.log('[Apelles] looks like you errored, probably incorrect parameters. hint: use `disableBatching` to help debug');
    console.error(e);
  } finally {
    batched = [];
  }
}
register('renderWorld', uploadBatched).setPriority(Priority.LOWEST);
/**
 * use if you need a more detailed error message (sc. you are passing the wrong parameters and getting a class cast exception and need to find the exact method call)
 */
export function disableBatching() {
  batchCalls = false;
}

/**
 * packed int is RGBA
 * @typedef {number | [number, number, number] | [number, number, number, number]} ColorLike
 */

/**
 * @typedef RenderOptions
 * @property {number} [lighting=0] `0` - 0: none | 1: smooth | 2: flat
 * @property {boolean} [phase=false] `false`
 * @property {boolean} [cull=true] `true` - whether to frustum cull the object. you should only disable this if you know what you are doing. though i doubt there will be any false positives, the option is here
 * @property {number} [chroma=0] `0` - use chroma, the color will be interpreted as [chromaSize, speed, lightness & chroma, alpha] (hint, use `packChromaParams`). 0: no chroma | 1: 2d chroma | 2: 3d chroma
 */

// why was jsdoc being a bitch idk idc
/**
 * @typedef LineOptions
 * @property {number} [lw=1] `1` - width of stroke
 * @property {boolean} [smooth=false] `false` - whether to enable `GL_LINE_SMOOTH`
 */

/**
 * @typedef FaceOptions
 * @property {boolean} [backfaceCull=true] `true` - whether to cull back-facing faces
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
 * @typedef StringOptions
 * @property {number} [scale=1] `1`
 * @property {boolean} [increase=false] `false` - increase with distance
 * @property {boolean} [shadow=true] `true` - backdrop shadow
 * @property {number} [blackBox=1] `1` - gray box behind text, 0: no box | 1: box around entire text | 2: box around each line
 * @property {number} [anchor=0] `5` - how text is positioned around the point. [visual](https://i.imgur.com/GRwBx0H.png) 0: top left | 1: middle left | 2: bottom left | 4: top middle | 5: center | 6: bottom middle | 8: top right | 9: middle right | 10: bottom right
 * @property {number} [alignX=0] `2` - how text is aligned on the x-axis. [visual](https://i.imgur.com/YWWa3AL.png) 0: left | 1: right | 2: center
 * @property {number} [parseMode=3] `3` - how formatting is parsed, 0: ignore | 1: only parse § | 2: only parse § but ignore effects | 3: parse both § and & | 4: parse both § and & but ignore effects
 * @property {number} [lineHeight=9] `9` - height of each line (not height of characters, not affected by scaling). each character is 8 units tall
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
 * @param {RenderOptions & LineOptions} options
 */
export function renderLine(color, points, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([1, color, 3, points, lw, lighting, phase, smooth, cull, true, chroma]);
  else addPrimitive.call(APRendererI, color, 3, points, lw, lighting, phase, smooth, cull, true, chroma);
}

/**
 * tip: if `points` does not change often, using `java.util.List` over native js arrays will offer better performance
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} mode GL primitive constant
 * @param {[number, number, number][]} points
 * @param {RenderOptions & LineOptions & FaceOptions} options
 */
export function renderPrimitive(color, mode, points, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([1, color, mode, points, lw, lighting, phase, smooth, cull, backfaceCull, chroma]);
  else addPrimitive.call(APRendererI, color, mode, points, lw, lighting, phase, smooth, cull, backfaceCull, chroma);
}

const addBoxO = APRendererI.addBoxO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {RenderOptions & LineOptions & AABBOptions} options
 */
export function renderBoxOutline(color, x, y, z, w, h, { centered = true, wz = w, lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([2, color, x, y, z, w, h, wz, centered, lw, lighting, phase, smooth, cull, chroma]);
  else addBoxO.call(APRendererI, color, x, y, z, w, h, wz, centered, lw, lighting, phase, smooth, cull, chroma);
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
 * @param {RenderOptions & LineOptions} options
 */
export function renderAABBOutline(color, x1, y1, z1, x2, y2, z2, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([
    3,
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
  ]);
  else addAABBO.call(APRendererI,
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

const addAABBOM = APRendererI.addAABBOM ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param aabb net.minecraft.util.AxisAlignedBB
 * @param {RenderOptions & LineOptions} options
 */
export function renderMCAABBOutline(color, aabb, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([30, color, aabb, lw, lighting, phase, smooth, cull, chroma]);
  else addAABBOM.call(APRendererI, color, lw, lighting, phase, smooth, cull, chroma);
}

const addBoxF = APRendererI.addBoxF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {RenderOptions & FaceOptions & AABBOptions} options
 */
export function renderBoxFilled(color, x, y, z, w, h, { centered = true, wz = w, lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([4, color, x, y, z, w, h, wz, centered, lighting, phase, cull, backfaceCull, chroma]);
  else addBoxF.call(APRendererI, color, x, y, z, w, h, wz, centered, lighting, phase, cull, backfaceCull, chroma);
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
 * @param {RenderOptions & FaceOptions} options
 */
export function renderAABBFilled(color, x1, y1, z1, x2, y2, z2, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([
    5,
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
    backfaceCull,
    chroma
  ]);
  else addAABBF.call(APRendererI,
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
    backfaceCull,
    chroma
  );
}

const addAABBFM = APRendererI.addAABBFM ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param aabb net.minecraft.util.AxisAlignedBB
 * @param {RenderOptions & FaceOptions} options
 */
export function renderMCAABBFilled(color, aabb, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([31, color, aabb, lighting, phase, cull, backfaceCull, chroma]);
  else addAABBFM.call(APRendererI, color, aabb, lighting, phase, cull, backfaceCull, chroma);
}

const addBeacon = APRendererI.addBeacon ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {RenderOptions & FaceOptions & BeaconOptions} options
 */
export function renderBeacon(color, x, y, z, { centered = true, h = 300 - y, lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (!centered) {
    x += 0.5;
    z += 0.5;
  }
  if (batchCalls) batched.push([6, color, x, y, z, h, lighting, phase, cull, backfaceCull, chroma]);
  else addBeacon.call(APRendererI, color, x, y, z, h, lighting, phase, cull, backfaceCull, chroma);
}

const addCircle = APRendererI.addCircle ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} r
 * @param {number} segments
 * @param {RenderOptions & LineOptions} options
 */
export function renderCircle(color, x, y, z, r, segments, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([7, color, x, y, z, r, segments, lw, lighting, phase, smooth, cull, chroma]);
  else addCircle.call(APRendererI, color, x, y, z, r, segments, lw, lighting, phase, smooth, cull, chroma);
}

const addIcosphere = APRendererI.addIcosphere ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} r
 * @param {number} divisions - warning, scales exponentially (0 = 20 triangles, 1 = 80, 2 = 320, 3 = 1280)
 * @param {RenderOptions & FaceOptions} options
 */
export function renderSphere(color, x, y, z, r, divisions, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([8, color, x, y, z, r, divisions, lighting, phase, cull, backfaceCull, chroma]);
  else addIcosphere.call(APRendererI, color, x, y, z, r, divisions, lighting, phase, cull, backfaceCull, chroma);
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
 * @param {RenderOptions & LineOptions} options
 */
export function renderPyramidOutline(color, x, y, z, r, h, n, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([9, color, x, y, z, r, h, n, lw, lighting, phase, smooth, cull, chroma]);
  else addPyramidO.call(APRendererI, color, x, y, z, r, h, n, lw, lighting, phase, smooth, cull, chroma);
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
 * @param {RenderOptions & FaceOptions} options
 */
export function renderPyramidFilled(color, x, y, z, r, h, n, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([10, color, x, y, z, r, h, n, lighting, phase, cull, backfaceCull, chroma]);
  else addPyramidF.call(APRendererI, color, x, y, z, r, h, n, lighting, phase, cull, backfaceCull, chroma);
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
 * @param {RenderOptions & FaceOptions} options
 */
export function renderVerticalCylinder(color, x, y, z, r, h, segments, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([11, color, x, y, z, r, h, segments, lighting, phase, cull, backfaceCull, chroma]);
  else addVertCylinder.call(APRendererI, color, x, y, z, r, h, segments, lighting, phase, cull, backfaceCull, chroma);
}

const addOctahedronO = APRendererI.addOctahedronO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {RenderOptions & LineOptions} options
 */
export function renderOctahedronOutline(color, x, y, z, w, h, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([12, color, x, y, z, w / 2, h / 2, lw, lighting, phase, smooth, cull, chroma]);
  else addOctahedronO.call(APRendererI, color, x, y, z, w / 2, h / 2, lw, lighting, phase, smooth, cull, chroma);
}

const addOctahedronF = APRendererI.addOctahedronF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {RenderOptions & FaceOptions} options
 */
export function renderOctahedronFilled(color, x, y, z, w, h, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([13, color, x, y, z, w / 2, h / 2, lighting, phase, cull, backfaceCull, chroma]);
  else addOctahedronF.call(APRendererI, color, x, y, z, w / 2, h / 2, lighting, phase, cull, backfaceCull, chroma);
}

const addStraightStairO = APRendererI.addStraightStairO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {RenderOptions & LineOptions} options
 */
export function renderStraightStairOutline(color, x, y, z, type, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([14, color, x, y, z, type, lighting, phase, cull, chroma]);
  else addStraightStairO.call(APRendererI, color, x, y, z, type, lw, lighting, phase, smooth, cull, chroma);
}

const addStraightStairF = APRendererI.addStraightStairF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {RenderOptions & FaceOptions} options
 */
export function renderStraightStairFilled(color, x, y, z, type, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([15, color, x, y, z, type, lighting, phase, cull, backfaceCull, chroma]);
  else addStraightStairF.call(APRendererI, color, x, y, z, type, lighting, phase, cull, backfaceCull, chroma);
}

const addInnerStairO = APRendererI.addInnerStairO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {boolean} left is the stair shape `INNER_LEFT`
 * @param {RenderOptions & LineOptions} options
 */
export function renderInnerStairOutline(color, x, y, z, type, left, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([16, color, x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma]);
  else addInnerStairO.call(APRendererI, color, x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma);
}

const addInnerStairF = APRendererI.addInnerStairF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {boolean} left is the stair shape `INNER_LEFT`
 * @param {RenderOptions & FaceOptions} options
 */
export function renderInnerStairFilled(color, x, y, z, type, left, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([17, color, x, y, z, type, left, lighting, phase, cull, backfaceCull, chroma]);
  else addInnerStairF.call(APRendererI, color, x, y, z, type, left, lighting, phase, cull, backfaceCull, chroma);
}

const addOuterStairO = APRendererI.addOuterStairO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {boolean} left is the stair shape `OUTER_LEFT`
 * @param {RenderOptions & LineOptions} options
 */
export function renderOuterStairOutline(color, x, y, z, type, left, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([18, color, x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma]);
  else addOuterStairO.call(APRendererI, color, x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma);
}

const addOuterStairF = APRendererI.addOuterStairF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type int (metadata)
 * @param {boolean} left is the stair shape `OUTER_LEFT`
 * @param {RenderOptions & FaceOptions} options
 */
export function renderOuterStairFilled(color, x, y, z, type, left, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([19, color, x, y, z, type, left, lighting, phase, cull, backfaceCull, chroma]);
  else addOuterStairF.call(APRendererI, color, x, y, z, type, left, lighting, phase, cull, backfaceCull, chroma);
}

const addStairO = APRendererI.addStairO ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {RenderOptions & LineOptions} options
 */
export function renderStairOutline(color, x, y, z, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([20, color, x, y, z, lw, lighting, phase, smooth, cull, chroma]);
  else addStairO.call(APRendererI, color, x, y, z, lw, lighting, phase, smooth, cull, chroma);
}

const addStairF = APRendererI.addStairF ?? throwExp('bad');
/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {RenderOptions & FaceOptions} options
 */
export function renderStairFilled(color, x, y, z, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([24, color, x, y, z, lighting, phase, cull, backfaceCull, chroma]);
  else addStairF.call(APRendererI, color, x, y, z, lighting, phase, cull, backfaceCull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param bp net.minecraft.util.BlockPos
 * @param {RenderOptions & LineOptions} options
 */
export function renderStairOutlineBP(color, bp, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([21, color, bp, lw, lighting, phase, smooth, cull, chroma]);
  else addStairO.call(APRendererI, color, bp, lw, lighting, phase, smooth, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param bp net.minecraft.util.BlockPos
 * @param {RenderOptions & FaceOptions} options
 */
export function renderStairFilledBP(color, bp, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([25, color, bp, lighting, phase, cull, backfaceCull, chroma]);
  else addStairF.call(APRendererI, color, bp, lighting, phase, cull, backfaceCull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param bs net.minecraft.block.state.IBlockState
 * @param {RenderOptions & LineOptions} options
 */
export function renderStairOutlineBS(color, x, y, z, bs, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([22, color, x, y, z, bs, lw, lighting, phase, smooth, cull, chroma]);
  else addStairO.call(APRendererI, color, x, y, z, bs, lw, lighting, phase, smooth, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param bs net.minecraft.block.state.IBlockState
 * @param {RenderOptions & FaceOptions} options
 */
export function renderStairFilledBS(color, x, y, z, bs, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([26, color, x, y, z, bs, lighting, phase, cull, backfaceCull, chroma]);
  else addStairF.call(APRendererI, color, x, y, z, bs, lighting, phase, cull, backfaceCull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type MSB [set if not straight] | [set if outer] | [set if left] | [rest of metadata (length 3)] LSB
 * @param {RenderOptions & LineOptions} options
 */
export function renderStairOutlineManual(color, x, y, z, type, { lw = 1, lighting = 0, phase = false, smooth = false, cull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([23, color, x, y, z, type, lw, lighting, phase, smooth, cull, chroma]);
  else addStairO.call(APRendererI, color, x, y, z, type, lw, lighting, phase, smooth, cull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x int (BlockPos)
 * @param {number} y int (BlockPos)
 * @param {number} z int (BlockPos)
 * @param {number} type MSB [set if not straight] | [set if outer] | [set if left] | [rest of metadata (length 3)] LSB
 * @param {RenderOptions & FaceOptions} options
 */
export function renderStairFilledManual(color, x, y, z, type, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([27, color, x, y, z, type, lighting, phase, cull, backfaceCull, chroma]);
  else addStairF.call(APRendererI, color, x, y, z, type, lighting, phase, cull, backfaceCull, chroma);
}

const addBoxOJ = APRendererI.addBoxOJ ?? throwExp('bad');
/**
 * box but corners are miter-joined to look better with high lw
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {number} lw warning: `lw` units are different than all other methods; measured in 1/16ths of a block
 * @param {RenderOptions & FaceOptions & AABBOptions} options
 */
export function renderBoxOutlineMiter(color, x, y, z, w, h, lw, { centered = true, wz = w, lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([28, color, x, y, z, w, h, wz, centered, lw, lighting, phase, cull, backfaceCull, chroma]);
  else addBoxOJ.call(APRendererI, color, x, y, z, w, h, wz, centered, lw, lighting, phase, cull, backfaceCull, chroma);
}

const addAABBOJ = APRendererI.addAABBOJ ?? throwExp('bad');
/**
 * box but corners are miter-joined to look better with high lw
 *
 * warning: `lw` units are different than all other methods
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x1
 * @param {number} y1
 * @param {number} z1
 * @param {number} x2
 * @param {number} y2
 * @param {number} z2
 * @param {number} lw warning: `lw` units are different than all other methods; measured in 1/16ths of a block
 * @param {RenderOptions & FaceOptions} options
 */
export function renderAABBOutlineMiter(color, x1, y1, z1, x2, y2, z2, lw, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([
    29,
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
    cull,
    backfaceCull,
    chroma
  ]);
  else addAABBOJ.call(APRendererI,
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
    cull,
    backfaceCull,
    chroma
  );
}

const addAABBOJM = APRendererI.addAABBOJM ?? throwExp('bad');
/**
 * box but corners are miter-joined to look better with high lw
 *
 * warning: `lw` units are different than all other methods
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param aabb net.minecraft.util.AxisAlignedBB
 * @param {number} lw warning: `lw` units are different than all other methods; measured in 1/16ths of a block
 * @param {RenderOptions & FaceOptions} options
 */
export function renderMCAABBOutlineMiter(color, aabb, lw, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([32, color, aabb, lw, lighting, phase, cull, backfaceCull, chroma]);
  else addAABBOJM.call(APRendererI, color, aabb, lw, lighting, phase, cull, backfaceCull, chroma);
}

const addBillboard = APRendererI.addBillboard ?? throwExp('bad');
/**
 * rectangle that rotates with the player
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @param {number} h
 * @param {RenderOptions & FaceOptions} options
 */
export function renderBillboard(color, x, y, z, w, h, { lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([33, color, x, y, z, w, h, lighting, phase, cull, backfaceCull, chroma]);
  else addBillboard.call(APRendererI, color, x, y, z, w, h, lighting, phase, cull, backfaceCull, chroma);
}

const addString = APRendererI.addString ?? throwExp('bad');
/**
 * @param {ColorLike} color color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {string} string
 * @param {number} x position anchor
 * @param {number} y position anchor
 * @param {number} z position anchor
 * @param {number} rx right-facing vector
 * @param {number} ry right-facing vector
 * @param {number} rz right-facing vector
 * @param {number} dx down-facing vector
 * @param {number} dy down-facing vector
 * @param {number} dz down-facing vector
 * @param {number} nx normal vector
 * @param {number} ny normal vector
 * @param {number} nz normal vector
 * @param {RenderOptions & FaceOptions & StringOptions} options
 */
export function renderString(color, string, x, y, z, rx, ry, rz, dx, dy, dz, nx, ny, nz, { scale = 1, increase = false, shadow = true, blackBox = 1, anchor = 5, alignX = 2, parseMode = 3, lineHeight = 9, lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([34, color, string, x, y, z, rx, ry, rz, dx, dy, dz, nx, ny, nz, scale, increase, shadow, blackBox, anchor, alignX, parseMode, lineHeight, lighting, phase, cull, backfaceCull, chroma]);
  else addString.call(APRendererI, color, string, x, y, z, rx, ry, rz, dx, dy, dz, nx, ny, nz, scale, increase, shadow, blackBox, anchor, alignX, parseMode, lineHeight, lighting, phase, cull, backfaceCull, chroma);
}

const addBillboardString = APRendererI.addBillboardString ?? throwExp('bad');
/**
 * will always face player
 * @param {ColorLike} color color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {string} string
 * @param {number} x position anchor
 * @param {number} y position anchor
 * @param {number} z position anchor
 * @param {RenderOptions & FaceOptions & StringOptions} options
 */
export function renderBillboardString(color, string, x, y, z, { scale = 1, increase = false, shadow = true, blackBox = 1, anchor = 5, alignX = 2, parseMode = 3, lineHeight = 9, lighting = 0, phase = false, cull = true, backfaceCull = true, chroma = 0 } = {}) {
  if (batchCalls) batched.push([35, color, string, x, y, z, scale, increase, shadow, blackBox, anchor, alignX, parseMode, lineHeight, lighting, phase, cull, backfaceCull, chroma]);
  else addBillboardString.call(APRendererI, color, string, x, y, z, scale, increase, shadow, blackBox, anchor, alignX, parseMode, lineHeight, lighting, phase, cull, backfaceCull, chroma);
}

/**
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {RenderOptions & LineOptions} options
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

const _bindTexture = GlState.bindTexture ?? throwExp('bad');
/**
 * @overload
 * @param {string} tex `ResourceLocation` or texture path, e.g. `'textures/entity/beacon_beam.png'`
 *
 * @overload
 * @param {number} id
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
 * @param {number} mode 0: none | 1: smooth | 2: flat
 */
export function lighting(mode) {
  _setLighting.call(GlState, mode);
}

const _setBackfaceCull = GlState['setBackfaceCull(boolean)'] ?? throwExp('bad');
/**
 * @param {boolean} cull
 */
export function backfaceCull(cull) {
  _setBackfaceCull.call(GlState, cull);
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
 * @property {number} [width=1] `1` - int in pixels (can only be used with JFA)
 * @property {boolean} [phase=false] `false`
 * @property {boolean} [chroma=false] `false` - use chroma, the color will be interpreted as [chromaSize, speed, lightness & chroma, alpha] (hint, use `packChromaParams`). only 2d chroma :(
 * @property {boolean} [blackOutline=false] `false` - outermost pixel is a black outline (can only be used with JFA)
 * @property {boolean} [absoluteSize=false] `false` - if true pixel scales with size in world (distance) (can only be used with JFA)
 * @property {boolean} [renderInvis=false] `false` - if true will still render outline invisible entities
 */

const ManualOutliner = JavaTypeOrNull('com.perseuspotter.apelles.outline.outliner.ManualOutliner') ?? throwExp('jar not loaded correctly');
/**
 * INITIALLY UNREGISTERED
 *
 * must manually add entities to be outlined with `.add()`/`.remove()`
 * @param {ColorLike} color packed int (RGBA) or float[] (length 3/4, all [0, 1])
 * @param {number} type - type of outlining, 1: JFA | 2: Roberts Cross | 3: Sobel | 4: Blur (note: `width`, `absoluteSize`, and `blackOutline` can only be used with JFA)
 * @param {OutlineRenderOptions} options
 * @returns {{ setColor(color: number): void, setColor(color: [number, number, number] | [number, number, number, number]): void, add(ent: MCEntity): void, remove(ent: MCEntity): void, clear(): void, register(): void, unregister(): void }}
 */
export function createManualOutliner(color, type, { width = 1, phase = false, chroma = false, blackOutline = false, absoluteSize = false, renderInvis = false } = {}) {
  const o = new ManualOutliner(coerceColor(color), type, width, phase, chroma, blackOutline, absoluteSize, renderInvis);
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
 * @param {number} type - type of outlining, 1: JFA | 2: Roberts Cross | 3: Sobel | 4: Blur (note: `width`, `absoluteSize`, and `blackOutline` can only be used with JFA)
 * @param {OutlineRenderOptions} options
 * @returns {{ setColor(color: number): void, setColor(color: [number, number, number] | [number, number, number, number]): void, register(): void, unregister(): void, clear(): void }}
 */
export function createPerEntityOutliner(tester, color, type, { width = 1, phase = false, chroma = false, blackOutline = false, absoluteSize = false, renderInvis = false } = {}) {
  const o = new PerEntityOutliner(tester, coerceColor(color), type, width, phase, chroma, blackOutline, absoluteSize, renderInvis);
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
 * @param {number} type - type of outlining, 1: JFA | 2: Roberts Cross | 3: Sobel | 4: Blur (note: `width`, `absoluteSize`, and `blackOutline` can only be used with JFA)
 * @param {OutlineRenderOptions} options
 * @returns {{ setColor(color: number): void, setColor(color: [number, number, number] | [number, number, number, number]): void, register(): void, unregister(): void }}
 */
export function createPerFrameOutliner(tester, color, type, { width = 1, phase = false, chroma = false, blackOutline = false, absoluteSize = false, renderInvis = false } = {}) {
  const o = new PerFrameOutliner(tester, coerceColor(color), type, width, phase, chroma, blackOutline, absoluteSize, renderInvis);
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
 * @param {number} type - type of outlining, 1: JFA | 2: Roberts Cross | 3: Sobel | 4: Blur (note: `width`, `absoluteSize`, and `blackOutline` can only be used with JFA)
 * @param {OutlineRenderOptions} options
 * @returns {{ setColor(color: number): void, setColor(color: [number, number, number] | [number, number, number, number]): void, register(): void, unregister(): void, clear(): void, add(ent: MCEntity): void, remove(ent: MCEntity): void, retest(ent: MCEntity): void }}
 */
export function createSemiAutomaticOutliner(tester, color, type, { width = 1, phase = false, chroma = false, blackOutline = false, absoluteSize = false, renderInvis = false } = {}) {
  const o = new SemiAutomaticOutliner(tester, coerceColor(color), type, width, phase, chroma, blackOutline, absoluteSize, renderInvis);
  outliners.push(o);
  return o;
}

// require('./test');

register('command', () => Client.scheduleTask(20, () => Java.type('com.perseuspotter.apelles.outline.EntityOutliner').dump = true)).setName('apellesdumpfbo', true);
register('command', t => Client.scheduleTask(() => APRenderer.USE_NEW_SHIT = !!t)).setName('apellesusecompat', true);