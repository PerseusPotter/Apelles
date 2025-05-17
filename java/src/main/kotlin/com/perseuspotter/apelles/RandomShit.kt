package com.perseuspotter.apelles

import net.minecraft.block.BlockStairs
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.util.BlockPos

object RandomShit {
    fun comprehensiveStairMetadata(bs: IBlockState, bp: BlockPos): Int {
        if (bs.block !is BlockStairs) return -1
        val meta = bs.block.getMetaFromState(bs)
        val shape = bs.block.getActualState(bs, Minecraft.getMinecraft().theWorld, bp).getValue(BlockStairs.SHAPE)
        return when (shape) {
            BlockStairs.EnumShape.STRAIGHT, null -> meta
            BlockStairs.EnumShape.INNER_RIGHT -> meta or 0b100000
            BlockStairs.EnumShape.INNER_LEFT  -> meta or 0b101000
            BlockStairs.EnumShape.OUTER_RIGHT -> meta or 0b110000
            BlockStairs.EnumShape.OUTER_LEFT  -> meta or 0b111000
        }
    }
}