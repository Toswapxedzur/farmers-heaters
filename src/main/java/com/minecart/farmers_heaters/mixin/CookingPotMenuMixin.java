package com.minecart.farmers_heaters.mixin;

import com.minecart.farmers_heaters.mixin_interface.ISuperWithoutLevelHeatable;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

@Debug(export = true)
@Mixin(value = CookingPotMenu.class, remap = false)
// 1.20.1: RecipeBookMenu only takes ONE generic parameter (the container type)
public abstract class CookingPotMenuMixin extends RecipeBookMenu<RecipeWrapper> implements ISuperWithoutLevelHeatable {

    @Shadow @Final private ContainerData cookingPotData;

    @Shadow @Final public CookingPotBlockEntity blockEntity;

    public CookingPotMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    // 1.20.1: Replaced @Overwrite with a much safer @Inject
    // remap = false is required since getCookProgressionScaled is a Farmer's Delight method
    @Inject(method = "getCookProgressionScaled", at = @At("HEAD"), cancellable = true, remap = false)
    public void farmers_heaters$getCookProgressionScaled(CallbackInfoReturnable<Integer> cir) {
        int i = Math.max(cookingPotData.get(0), cookingPotData.get(2));
        int j = cookingPotData.get(1);

        cir.setReturnValue(j != 0 && i != 0 ? i * 24 / j : 0);
    }

    public boolean isSuperHeated() {
        return ((ISuperWithoutLevelHeatable) this.blockEntity).isSuperHeated();
    }
}
