package com.minecart.farmers_heaters.mixin_interface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ISuperHeatable {
    public boolean isSuperHeated(Level level, BlockPos pos);
}
