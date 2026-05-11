package com.minecart.farmers_heaters.mixin;

import com.minecart.farmers_heaters.mixin_interface.ISuperHeatable;
import com.minecart.farmers_heaters.mixin_interface.ISuperWithoutLevelHeatable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.*;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.awt.*;

@Debug(export = true)
@Mixin(CookingPotScreen.class)
public abstract class CookingPotScreenMixin extends AbstractContainerScreen<CookingPotMenu> implements RecipeUpdateListener {
    @Shadow @Final private static Rectangle HEAT_ICON;

    @Shadow
    @Final
    private static ResourceLocation BACKGROUND_TEXTURE;

    @Shadow
    @Final
    private static Rectangle PROGRESS_ARROW;

    public CookingPotScreenMixin(CookingPotMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Overwrite
    private void renderHeatIndicatorTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if (this.isHovering(HEAT_ICON.x, HEAT_ICON.y, HEAT_ICON.width, HEAT_ICON.height, (double)mouseX, (double)mouseY)) {
            String key = "container.cooking_pot." +
                    ( ((ISuperWithoutLevelHeatable)(CookingPotMenu)this.menu).isSuperHeated() ? "super_heated" : (((CookingPotMenu)this.menu).isHeated() ? "heated" : "not_heated") );
            gui.renderTooltip(this.font, TextUtils.getTranslation(key, new Object[0]), mouseX, mouseY);
        }
    }

    @Overwrite
    protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            gui.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            if(((ISuperWithoutLevelHeatable)(CookingPotMenu)this.menu).isSuperHeated()){
                gui.blit(BACKGROUND_TEXTURE, this.leftPos + HEAT_ICON.x, this.topPos + HEAT_ICON.y, 193, 0, HEAT_ICON.width, HEAT_ICON.height);
            }
            else if (((CookingPotMenu)this.menu).isHeated()) {
                gui.blit(BACKGROUND_TEXTURE, this.leftPos + HEAT_ICON.x, this.topPos + HEAT_ICON.y, 176, 0, HEAT_ICON.width, HEAT_ICON.height);
            }

            int l = ((CookingPotMenu)this.menu).getCookProgressionScaled();
            gui.blit(BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW.x, this.topPos + PROGRESS_ARROW.y, 176, 15, l + 1, PROGRESS_ARROW.height);
        }
    }
}
