import { begin, clipLine, createPassthroughOutlineTester, createPerEntityOutliner, draw, isInView, packChromaParams, pos, renderAABBOutline, renderBeacon, renderBillboard, renderBillboardString, renderBoxFilled, renderBoxOutline, renderBoxOutlineMiter, renderCircle, renderLine, renderOctahedronFilled, renderOctahedronOutline, renderPrimitive, renderPyramidFilled, renderPyramidOutline, renderSphere, renderStairFilled, renderStairOutline, renderString, renderTracer, renderVerticalCylinder } from './index';

const egg = r => {
  let guess = Math.sqrt(1 - 2 * r * r);
  // something something approximate y^2 + 0.64 * 1.4 ** 2r - 1 = 0 but lazy
  return [-guess, guess];
};

register('renderWorld', () => {
  renderPrimitive(0xFF000040, 4, [
    [20, 10, 0], [19, 10, 0], [19, 10, 1],
    [19, 10, 0], [19, 10, 1], [20, 10, 1]
  ]);
  renderLine(0x00FF00FF, [
    [-6, 10, -6],
    [-6, 10, +6],
    [+6, 10, +6],
    [+6, 10, -6],
    [-6, 10, -6],
  ], { lw: 5 });
  for (let x = -5; x <= 5; x += 2) {
    for (let z = -5; z <= 5; z += 2) {
      renderBoxFilled(packChromaParams(0.1, 0.3, 0.8, 0.35, 0.5), x, 10, z, 1, 1, { chroma: 1 });
      renderBoxOutline(packChromaParams(1, 0.3, 0.8, 0.35, 1), x, 10, z, 1, 1, { chroma: 2, lw: 4 });
      renderBeacon(0xFFFFFFFF, x, 11, z, { h: 10 });
    }
  }
  // renderTracer(0x000000FF, 0, 5, 0);
  renderCircle(0xFF00FFFF, 20, 5, 20, 3, 20, { lw: 3 });
  renderSphere(0x007FFFFF, 10, 10, 20, 4, 0, { lighting: 2 });
  renderSphere(0x007FFFFF, 10, 20, 20, 4, 1, { lighting: 2 });
  renderSphere(0x007FFFFF, 10, 30, 20, 4, 2, { lighting: 2 });
  renderSphere(0x007FFFFF, 10, 40, 20, 4, 3, { lighting: 2 });

  renderPyramidOutline(0xD11D05FF, 10, 10, 30, 2, 4, 3);
  renderPyramidOutline(0xD11D05FF, 10, 15, 30, 2, 4, 4);
  renderPyramidOutline(0xD11D05FF, 10, 25, 30, 2, -4, 8, { lw: 5 });
  renderPyramidFilled(0xD11D05FF, 20, 10, 30, 2, 4, 3, { lighting: 2 });
  renderPyramidFilled(0xD11D05FF, 20, 15, 30, 2, 4, 4, { lighting: 2 });
  renderPyramidFilled(0xD11D05FF, 20, 25, 30, 2, -4, 8, { lighting: 2 });

  renderVerticalCylinder(0xC0FF33A0, 30, 10, 30, 1, 4, 20, { lighting: 1 });

  renderOctahedronOutline(0xDADD1EFF, 30, 10, 10, 2, 3);
  renderOctahedronFilled(0xDADD1EFF, 30, 15, 10, 3, 4, { lighting: 2 });

  for (let d = 0; d < 5; d++) {
    for (let i = 0; i < 8; i++) {
      renderStairOutline(0xBADB01FF, i * 4, 10, -10 - d * 4, { lw: 2 });
      renderStairFilled(0xD1AB1080, i * 4, 10, -10 - d * 4);
    }
  }

  const eggCenter = [-10, 10, -10];
  const eggScale = 3;
  const beaconDensity = 0.3;
  for (let x = -eggScale; x <= eggScale; x += beaconDensity) {
    for (let z = -eggScale; z <= eggScale; z += beaconDensity) {
      let [y1, y2] = egg(Math.hypot(x, z) / eggScale);
      if (Number.isNaN(y1) || Number.isNaN(y2)) continue;
      renderBeacon(packChromaParams(1, 0.3, 1.0, 0.1, 1), x + eggCenter[0], y1 + eggCenter[1], z + eggCenter[2], { h: (y2 - y1) * eggScale, chroma: 2 });
    }
  }

  renderBoxOutlineMiter(0x00FFFFFF, -50, 10, 0, 10, 10, 3);
  renderBoxOutline(0xFF0000FF, -50, 25, 0, 10, 10, { lw: 10 });

  renderBillboard(0x000000A0, 30, 10, 0, 2, 4);

  renderString(0xFFFFFFFF, 'this is a test string with some 中文\nnow for some &l&4SCARY&r &m&af&bo&cr&dm&ea&ft&0t&1i&2n&3g&r and &o&neven&r more &kshit', 30, 15, 0, 1, 0, 0, 0, -1, 0, 0, 0, 1, { backfaceCull: false });
  renderBillboardString(0xFFFFFFFF, 'this moves', 30, 17, 0);
});

const tester = createPassthroughOutlineTester();
tester.addBlacklist(net.minecraft.entity.item.EntityArmorStand);
// tester.addWhitelist(net.minecraft.entity.passive.EntityPig);
const outliner = createPerEntityOutliner(tester, packChromaParams(0.2, 0.4, 1, 0.3, 1), 3, { phase: true, chroma: true });
// outliner.register();

const tester2 = createPassthroughOutlineTester();
// tester2.addWhitelist(net.minecraft.entity.item.EntityArmorStand);
// tester2.addWhitelist(net.minecraft.entity.passive.EntityPig);
const outliner2 = createPerEntityOutliner(tester2, 0xFF0000FF, 1, { width: 2, phase: false, chroma: false, absoluteSize: true });
// outliner2.register();