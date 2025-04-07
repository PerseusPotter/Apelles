import { begin, clipLine, draw, isInView, pos, renderAABBOutline, renderBeacon, renderBoxFilled, renderBoxOutline, renderCircle, renderLine, renderOctahedronFilled, renderOctahedronOutline, renderPyramidFilled, renderPyramidOutline, renderSphere, renderTracer, renderVerticalCylinder } from './index';

const egg = r => {
  let guess = Math.sqrt(1 - 2 * r * r);
  // something something approximate y^2 + 0.64 * 1.4 ** 2r - 1 = 0 but lazy
  return [-guess, guess];
};

register('renderEntity', ent => {
  renderBoxOutline(0x00FFFFFF, ent.getRenderX(), ent.getRenderY(), ent.getRenderZ(), ent.getWidth(), ent.getHeight(), true, undefined, 3, 0, true);
});
register('renderWorld', () => {
  renderLine(0x00FF00FF, [
    [-6, 10, -6],
    [-6, 10, +6],
    [+6, 10, +6],
    [+6, 10, -6],
    [-6, 10, -6],
  ], { lw: 5 });
  for (let x = -5; x <= 5; x += 2) {
    for (let z = -5; z <= 5; z += 2) {
      renderBoxFilled(0xFF0000A0, x, 10, z, 1, 1);
      renderBoxOutline(0x00FFFFFF, x, 10, z, 1, 1);
      renderBeacon(0xFFFFFFFF, x, 11, z, { h: 10 });
    }
  }
  renderTracer(0x000000FF, 0, 5, 0);
  renderCircle(0xFF00FFFF, 20, 5, 20, 3, 20, { lw: 3 });
  renderSphere(0x007FFFFF, 10, 10, 20, 4, 0, { lighting: 2 });
  renderSphere(0x007FFFFF, 10, 20, 20, 4, 1, { lighting: 2 });
  renderSphere(0x007FFFFF, 10, 30, 20, 4, 2, { lighting: 2 });
  renderSphere(0x007FFFFF, 10, 40, 20, 4, 3, { lighting: 2 });

  renderPyramidOutline(0xD11D05FF, 10, 10, 30, 2, 4, 3);
  renderPyramidOutline(0xD11D05FF, 10, 15, 30, 2, 4, 4);
  renderPyramidOutline(0xD11D05FF, 10, 25, 30, 2, -4, 8);
  renderPyramidFilled(0xD11D05FF, 20, 10, 30, 2, 4, 3, { lighting: 2 });
  renderPyramidFilled(0xD11D05FF, 20, 15, 30, 2, 4, 4, { lighting: 2 });
  renderPyramidFilled(0xD11D05FF, 20, 25, 30, 2, -4, 8, { lighting: 2 });

  renderVerticalCylinder(0xC0FF33A0, 30, 10, 30, 1, 4, 20, { lighting: 1 });

  renderOctahedronOutline(0xDADD1EFF, 30, 10, 10, 2, 3);
  renderOctahedronFilled(0xDADD1EFF, 30, 15, 10, 3, 4, { lighting: 2 });

  const eggCenter = [-10, 10, -10];
  const eggScale = 3;
  const beaconDensity = 0.3;
  for (let x = -eggScale; x <= eggScale; x += beaconDensity) {
    for (let z = -eggScale; z <= eggScale; z += beaconDensity) {
      let [y1, y2] = egg(Math.hypot(x, z) / eggScale);
      if (Number.isNaN(y1) || Number.isNaN(y2)) continue;
      renderBeacon(0xF0EAD6FF, x + eggCenter[0], y1 + eggCenter[1], z + eggCenter[2], { h: (y2 - y1) * eggScale });
    }
  }
});