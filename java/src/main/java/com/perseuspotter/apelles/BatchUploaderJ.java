package com.perseuspotter.apelles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.util.List;

class BatchUploaderJ {
    // thank you rhino
    private static int asInt(Object v) {
        if (v instanceof Integer) {
            return (Integer) v;
        } else {
            return ((Double) v).intValue();
        }
    }

    private static double asDouble(Object v) {
        if (v instanceof Double) {
            return (Double) v;
        } else {
            return ((Integer) v).doubleValue();
        }
    }

    private static long asLong(Object v) {
        if (v instanceof Integer) {
            return ((Integer) v).longValue();
        } else {
            return ((Double) v).longValue();
        }
    }

    public static void upload(List<List<Object>> params) {
        params.forEach(arr -> {
            int type = asInt(arr.get(0));
            Object color = arr.get(1);
            boolean isArrCol = color instanceof List;
            switch (type) {
                case 1: {
                    if (isArrCol) Renderer.INSTANCE.addPrimitive(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            (List<List<Double>>) arr.get(3),
                            asDouble(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    else Renderer.INSTANCE.addPrimitive(
                            asLong(color),
                            asInt(arr.get(2)),
                            (List<List<Double>>) arr.get(3),
                            asDouble(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    break;
                }
                case 2: {
                    if (isArrCol) Renderer.INSTANCE.addBoxO(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            (Boolean) arr.get(8),
                            asDouble(arr.get(9)),
                            asInt(arr.get(10)),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            (Boolean) arr.get(13),
                            asInt(arr.get(14))
                    );
                    else Renderer.INSTANCE.addBoxO(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            (Boolean) arr.get(8),
                            asDouble(arr.get(9)),
                            asInt(arr.get(10)),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            (Boolean) arr.get(13),
                            asInt(arr.get(14))
                    );
                    break;
                }
                case 3: {
                    if (isArrCol) Renderer.INSTANCE.addAABBO(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asDouble(arr.get(8)),
                            asInt(arr.get(9)),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            asInt(arr.get(13))
                    );
                    else Renderer.INSTANCE.addAABBO(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asDouble(arr.get(8)),
                            asInt(arr.get(9)),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            asInt(arr.get(13))
                    );
                    break;
                }
                case 4: {
                    if (isArrCol) Renderer.INSTANCE.addBoxF(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            (Boolean) arr.get(8),
                            asInt(arr.get(9)),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            asInt(arr.get(13))
                    );
                    else Renderer.INSTANCE.addBoxF(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            (Boolean) arr.get(8),
                            asInt(arr.get(9)),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            asInt(arr.get(13))
                    );
                    break;
                }
                case 5: {
                    if (isArrCol) Renderer.INSTANCE.addAABBF(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    else Renderer.INSTANCE.addAABBF(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    break;
                }
                case 6: {
                    if (isArrCol) Renderer.INSTANCE.addBeacon(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    else Renderer.INSTANCE.addBeacon(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    break;
                }
                case 7: {
                    if (isArrCol) Renderer.INSTANCE.addCircle(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asInt(arr.get(6)),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    else Renderer.INSTANCE.addCircle(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asInt(arr.get(6)),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    break;
                }
                case 8: {
                    if (isArrCol) Renderer.INSTANCE.addIcosphere(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asInt(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    else Renderer.INSTANCE.addIcosphere(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asInt(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    break;
                }
                case 9: {
                    if (isArrCol) Renderer.INSTANCE.addPyramidO(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            asDouble(arr.get(8)),
                            asInt(arr.get(9)),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            asInt(arr.get(13))
                    );
                    else Renderer.INSTANCE.addPyramidO(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            asDouble(arr.get(8)),
                            asInt(arr.get(9)),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            asInt(arr.get(13))
                    );
                    break;
                }
                case 10: {
                    if (isArrCol) Renderer.INSTANCE.addPyramidF(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    else Renderer.INSTANCE.addPyramidF(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    break;
                }
                case 11: {
                    if (isArrCol) Renderer.INSTANCE.addVertCylinder(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    else Renderer.INSTANCE.addVertCylinder(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    break;
                }
                case 12: {
                    if (isArrCol) Renderer.INSTANCE.addOctahedronO(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    else Renderer.INSTANCE.addOctahedronO(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    break;
                }
                case 13: {
                    if (isArrCol) Renderer.INSTANCE.addOctahedronF(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    else Renderer.INSTANCE.addOctahedronF(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    break;
                }
                case 14: {
                    if (isArrCol) Renderer.INSTANCE.addStraightStairO(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    else Renderer.INSTANCE.addStraightStairO(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    break;
                }
                case 15: {
                    if (isArrCol) Renderer.INSTANCE.addStraightStairF(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    else Renderer.INSTANCE.addStraightStairF(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    break;
                }
                case 16: {
                    if (isArrCol) Renderer.INSTANCE.addInnerStairO(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    else Renderer.INSTANCE.addInnerStairO(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    break;
                }
                case 17: {
                    if (isArrCol) Renderer.INSTANCE.addInnerStairF(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    else Renderer.INSTANCE.addInnerStairF(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    break;
                }
                case 18: {
                    if (isArrCol) Renderer.INSTANCE.addOuterStairO(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    else Renderer.INSTANCE.addOuterStairO(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            asDouble(arr.get(7)),
                            asInt(arr.get(8)),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            asInt(arr.get(12))
                    );
                    break;
                }
                case 19: {
                    if (isArrCol) Renderer.INSTANCE.addOuterStairF(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    else Renderer.INSTANCE.addOuterStairF(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    break;
                }
                case 20: {
                    if (isArrCol) Renderer.INSTANCE.addStairO(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asDouble(arr.get(5)),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    else Renderer.INSTANCE.addStairO(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asDouble(arr.get(5)),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    break;
                }
                case 21: {
                    if (isArrCol) Renderer.INSTANCE.addStairO(
                            (List<Double>) color,
                            (BlockPos) arr.get(2),
                            asDouble(arr.get(3)),
                            asInt(arr.get(4)),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            asInt(arr.get(8))
                    );
                    else Renderer.INSTANCE.addStairO(
                            asLong(color),
                            (BlockPos) arr.get(2),
                            asDouble(arr.get(3)),
                            asInt(arr.get(4)),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            asInt(arr.get(8))
                    );
                    break;
                }
                case 22: {
                    if (isArrCol) Renderer.INSTANCE.addStairO(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            (IBlockState) arr.get(5),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    else Renderer.INSTANCE.addStairO(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            (IBlockState) arr.get(5),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    break;
                }
                case 23: {
                    if (isArrCol) Renderer.INSTANCE.addStairO(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    else Renderer.INSTANCE.addStairO(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    break;
                }
                case 24: {
                    if (isArrCol) Renderer.INSTANCE.addStairF(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            asInt(arr.get(9))
                    );
                    else Renderer.INSTANCE.addStairF(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            asInt(arr.get(9))
                    );
                    break;
                }
                case 25: {
                    if (isArrCol) Renderer.INSTANCE.addStairF(
                            (List<Double>) color,
                            (BlockPos) arr.get(2),
                            asInt(arr.get(3)),
                            (Boolean) arr.get(4),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            asInt(arr.get(7))
                    );
                    else Renderer.INSTANCE.addStairF(
                            asLong(color),
                            (BlockPos) arr.get(2),
                            asInt(arr.get(3)),
                            (Boolean) arr.get(4),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            asInt(arr.get(7))
                    );
                    break;
                }
                case 26: {
                    if (isArrCol) Renderer.INSTANCE.addStairF(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            (IBlockState) arr.get(5),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    else Renderer.INSTANCE.addStairF(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            (IBlockState) arr.get(5),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    break;
                }
                case 27: {
                    if (isArrCol) Renderer.INSTANCE.addStairF(
                            (List<Double>) color,
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    else Renderer.INSTANCE.addStairF(
                            asLong(color),
                            asInt(arr.get(2)),
                            asInt(arr.get(3)),
                            asInt(arr.get(4)),
                            asInt(arr.get(5)),
                            asInt(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            asInt(arr.get(10))
                    );
                    break;
                }
                case 28: {
                    if (isArrCol) Renderer.INSTANCE.addBoxOJ(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            (Boolean) arr.get(8),
                            asDouble(arr.get(9)),
                            asInt(arr.get(10)),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            (Boolean) arr.get(13),
                            asInt(arr.get(14))
                    );
                    else Renderer.INSTANCE.addBoxOJ(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            (Boolean) arr.get(8),
                            asDouble(arr.get(9)),
                            asInt(arr.get(10)),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            (Boolean) arr.get(13),
                            asInt(arr.get(14))
                    );
                    break;
                }
                case 29: {
                    if (isArrCol) Renderer.INSTANCE.addAABBOJ(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asDouble(arr.get(8)),
                            asInt(arr.get(9)),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            asInt(arr.get(13))
                    );
                    else Renderer.INSTANCE.addAABBOJ(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asDouble(arr.get(8)),
                            asInt(arr.get(9)),
                            (Boolean) arr.get(10),
                            (Boolean) arr.get(11),
                            (Boolean) arr.get(12),
                            asInt(arr.get(13))
                    );
                    break;
                }
                case 30: {
                    if (isArrCol) Renderer.INSTANCE.addAABBOM(
                            (List<Double>) color,
                            (AxisAlignedBB) arr.get(2),
                            asDouble(arr.get(3)),
                            asInt(arr.get(4)),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            asInt(arr.get(8))
                    );
                    else Renderer.INSTANCE.addAABBOM(
                            asLong(color),
                            (AxisAlignedBB) arr.get(2),
                            asDouble(arr.get(3)),
                            asInt(arr.get(4)),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            asInt(arr.get(8))
                    );
                    break;
                }
                case 31: {
                    if (isArrCol) Renderer.INSTANCE.addAABBFM(
                            (List<Double>) color,
                            (AxisAlignedBB) arr.get(2),
                            asInt(arr.get(3)),
                            (Boolean) arr.get(4),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            asInt(arr.get(7))
                    );
                    else Renderer.INSTANCE.addAABBFM(
                            asLong(color),
                            (AxisAlignedBB) arr.get(2),
                            asInt(arr.get(3)),
                            (Boolean) arr.get(4),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            asInt(arr.get(7))
                    );
                    break;
                }
                case 32: {
                    if (isArrCol) Renderer.INSTANCE.addAABBOJM(
                            (List<Double>) color,
                            (AxisAlignedBB) arr.get(2),
                            asDouble(arr.get(3)),
                            asInt(arr.get(4)),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            asInt(arr.get(8))
                    );
                    else Renderer.INSTANCE.addAABBOJM(
                            asLong(color),
                            (AxisAlignedBB) arr.get(2),
                            asDouble(arr.get(3)),
                            asInt(arr.get(4)),
                            (Boolean) arr.get(5),
                            (Boolean) arr.get(6),
                            (Boolean) arr.get(7),
                            asInt(arr.get(8))
                    );
                    break;
                }
                case 33: {
                    if (isArrCol) Renderer.INSTANCE.addBillboard(
                            (List<Double>) color,
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    else Renderer.INSTANCE.addBillboard(
                            asLong(color),
                            asDouble(arr.get(2)),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asInt(arr.get(7)),
                            (Boolean) arr.get(8),
                            (Boolean) arr.get(9),
                            (Boolean) arr.get(10),
                            asInt(arr.get(11))
                    );
                    break;
                }
                case 34: {
                    if (isArrCol) Renderer.INSTANCE.addString(
                            (List<Double>) color,
                            (String) arr.get(2),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asDouble(arr.get(8)),
                            asDouble(arr.get(9)),
                            asDouble(arr.get(10)),
                            asDouble(arr.get(11)),
                            asDouble(arr.get(12)),
                            asDouble(arr.get(13)),
                            asDouble(arr.get(14)),
                            asDouble(arr.get(15)),
                            (Boolean) arr.get(16),
                            (Boolean) arr.get(17),
                            asInt(arr.get(18)),
                            asInt(arr.get(19)),
                            asInt(arr.get(20)),
                            asInt(arr.get(21)),
                            asDouble(arr.get(22)),
                            asInt(arr.get(23)),
                            (Boolean) arr.get(24),
                            (Boolean) arr.get(25),
                            (Boolean) arr.get(26),
                            asInt(arr.get(27))
                    );
                    else Renderer.INSTANCE.addString(
                            asLong(color),
                            (String) arr.get(2),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            asDouble(arr.get(7)),
                            asDouble(arr.get(8)),
                            asDouble(arr.get(9)),
                            asDouble(arr.get(10)),
                            asDouble(arr.get(11)),
                            asDouble(arr.get(12)),
                            asDouble(arr.get(13)),
                            asDouble(arr.get(14)),
                            asDouble(arr.get(15)),
                            (Boolean) arr.get(16),
                            (Boolean) arr.get(17),
                            asInt(arr.get(18)),
                            asInt(arr.get(19)),
                            asInt(arr.get(20)),
                            asInt(arr.get(21)),
                            asDouble(arr.get(22)),
                            asInt(arr.get(23)),
                            (Boolean) arr.get(24),
                            (Boolean) arr.get(25),
                            (Boolean) arr.get(26),
                            asInt(arr.get(27))
                    );
                    break;
                }
                case 35: {
                    if (isArrCol) Renderer.INSTANCE.addBillboardString(
                            (List<Double>) color,
                            (String) arr.get(2),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            asInt(arr.get(9)),
                            asInt(arr.get(10)),
                            asInt(arr.get(11)),
                            asInt(arr.get(12)),
                            asDouble(arr.get(13)),
                            asInt(arr.get(14)),
                            (Boolean) arr.get(15),
                            (Boolean) arr.get(16),
                            (Boolean) arr.get(17),
                            asInt(arr.get(18))
                    );
                    else Renderer.INSTANCE.addBillboardString(
                            asLong(color),
                            (String) arr.get(2),
                            asDouble(arr.get(3)),
                            asDouble(arr.get(4)),
                            asDouble(arr.get(5)),
                            asDouble(arr.get(6)),
                            (Boolean) arr.get(7),
                            (Boolean) arr.get(8),
                            asInt(arr.get(9)),
                            asInt(arr.get(10)),
                            asInt(arr.get(11)),
                            asInt(arr.get(12)),
                            asDouble(arr.get(13)),
                            asInt(arr.get(14)),
                            (Boolean) arr.get(15),
                            (Boolean) arr.get(16),
                            (Boolean) arr.get(17),
                            asInt(arr.get(18))
                    );
                    break;
                }
            }
        });
    }
}