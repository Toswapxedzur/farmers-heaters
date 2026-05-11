package com.minecart.farmers_heaters.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.minecart.central_heater.block.stove.BrickStoveBlock;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.block.stove.StoneStoveBlock;
import com.minecart.central_heater.misc.enumeration.NetherFireState;
import com.minecart.farmers_heaters.mixin_interface.ISuperHeatable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

@Debug(export = true)
@Mixin(HeatableBlockEntity.class)
public interface HeatableBlockEntityMixin extends ISuperHeatable {
    @Inject(method = "isHeated", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0, shift = At.Shift.BY, by = 2), cancellable = true)
    public default void isHeated(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> returnable, @Local(ordinal = 0) BlockState stateBelow) {
        if(stateBelow.getBlock() instanceof BrickStoveBlock || stateBelow.getBlock() instanceof StoneStoveBlock){
            if(stateBelow.hasProperty(BlockStateProperties.LIT)) {
                returnable.setReturnValue(stateBelow.getValue(BlockStateProperties.LIT));
                return;
            }
        }else if(stateBelow.getBlock() instanceof GoldenStoveBlock){
            if(stateBelow.hasProperty(GoldenStoveBlock.LIT_SOUL)) {
                returnable.setReturnValue(stateBelow.getValue(GoldenStoveBlock.LIT_SOUL).equals(NetherFireState.BURN));
                return;
            }
        }
        returnable.setReturnValue(false);
    }

    @Unique
    public default boolean isSuperHeated(Level level, BlockPos pos){
        BlockState stateBelow = level.getBlockState(pos.below());
        if(stateBelow.getBlock() instanceof GoldenStoveBlock){
            if(stateBelow.hasProperty(GoldenStoveBlock.LIT_SOUL))
                return stateBelow.getValue(GoldenStoveBlock.LIT_SOUL).equals(NetherFireState.SOUL);
        }
        return false;
    }
}
