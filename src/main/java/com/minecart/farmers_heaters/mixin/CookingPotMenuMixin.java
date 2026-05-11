package com.minecart.farmers_heaters.mixin;

import com.minecart.farmers_heaters.mixin_interface.ISuperHeatable;
import com.minecart.farmers_heaters.mixin_interface.ISuperWithoutLevelHeatable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

@Debug(export = true)
@Mixin(CookingPotMenu.class)
public abstract class CookingPotMenuMixin extends RecipeBookMenu<RecipeWrapper, CookingPotRecipe> implements ISuperWithoutLevelHeatable {
    @Shadow @Final private ContainerData cookingPotData;

    @Shadow
    @Final
    public CookingPotBlockEntity blockEntity;

    public CookingPotMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Overwrite
    public int getCookProgressionScaled() {
        int i = Math.max(cookingPotData.get(0), cookingPotData.get(2));
        int j = cookingPotData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public boolean isSuperHeated() {
        return ((ISuperWithoutLevelHeatable) this.blockEntity).isSuperHeated();
    }
}
