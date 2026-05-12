package com.minecart.farmers_heaters.mixin;

import com.minecart.central_heater.block.stove.BrickStoveBlock;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.block.stove.StoneStoveBlock;
import com.minecart.central_heater.misc.enumeration.NetherFireState;
import com.minecart.farmers_heaters.mixin_interface.ISuperHeatable;
import com.minecart.farmers_heaters.mixin_interface.ISuperWithoutLevelHeatable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

@Debug(export = true)
@Mixin(value = HeatableBlockEntity.class, remap = false)
public interface HeatableBlockEntityMixin extends ISuperHeatable {

    /**
     * @author Farmers Heaters
     * @reason Replace Farmer's Delight default heat source check (which uses the
     *         farmersdelight:heat_sources tag) with an explicit allow-list of the
     *         three Central Heater stoves. This is intentional and matches the
     *         1.21.1 implementation (which short-circuits the tag check via a
     *         MixinExtras injection). Cooking pots now ONLY heat on top of a lit
     *         Brick, Stone, or Golden stove (with BURN soul state) — the whole
     *         "requires fuel to cook" feature of this addon.
     */
    @Overwrite
    default boolean isHeated(Level level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());
        if(stateBelow.getBlock() instanceof BrickStoveBlock || stateBelow.getBlock() instanceof StoneStoveBlock){
            if(stateBelow.hasProperty(BlockStateProperties.LIT)) {
                return stateBelow.getValue(BlockStateProperties.LIT);
            }
        }else if(stateBelow.getBlock() instanceof GoldenStoveBlock){
            if(stateBelow.hasProperty(GoldenStoveBlock.LIT_SOUL)) {
                return stateBelow.getValue(GoldenStoveBlock.LIT_SOUL).equals(NetherFireState.BURN);
            }
        }
        return false;
    }

    @Unique
    default boolean isSuperHeated(Level level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());
        if (stateBelow.getBlock() instanceof GoldenStoveBlock) {
            if (stateBelow.hasProperty(GoldenStoveBlock.LIT_SOUL)) {
                return stateBelow.getValue(GoldenStoveBlock.LIT_SOUL).equals(NetherFireState.SOUL);
            }
        }
        return false;
    }
}