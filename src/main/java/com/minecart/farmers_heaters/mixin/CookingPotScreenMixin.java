package com.minecart.farmers_heaters.mixin;

import com.minecart.farmers_heaters.FarmersHeaters;
import com.minecart.farmers_heaters.mixin_interface.ISuperWithoutLevelHeatable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.awt.*;

@Debug(export = true)
@Mixin(value = CookingPotScreen.class, remap = false)
public abstract class CookingPotScreenMixin extends AbstractContainerScreen<CookingPotMenu> implements RecipeUpdateListener {

    @Shadow @Final private static Rectangle HEAT_ICON;
    @Shadow @Final private static ResourceLocation BACKGROUND_TEXTURE;
    @Shadow @Final private static Rectangle PROGRESS_ARROW;

    // Dedicated texture for the soul-flame heat overlay. Lives in our own namespace because
    // Forge 1.20.1 stacks mod resource packs in mod-id order, and `farmersdelight` sorts after
    // `farmers_heaters` (alphabetically '_' < 'd'). That means our same-path override of
    // `farmersdelight:textures/gui/cooking_pot.png` gets clobbered by FD's stock texture, so
    // blitting the (193,0) region from BACKGROUND_TEXTURE would yield FD pixels (no soul flame).
    // Putting the asset in our own namespace sidesteps the conflict entirely.
    private static final ResourceLocation SUPER_HEATED_BACKGROUND_TEXTURE =
            new ResourceLocation(FarmersHeaters.MODID, "textures/gui/cooking_pot.png");

    public CookingPotScreenMixin(CookingPotMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    // 1.20.1: @Inject + cancel instead of @Overwrite. remap = false because this is a Farmer's Delight method.
    @Inject(method = "renderHeatIndicatorTooltip", at = @At("HEAD"), cancellable = true, remap = false)
    private void farmers_heaters$renderHeatIndicatorTooltip(GuiGraphics gui, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.isHovering(HEAT_ICON.x, HEAT_ICON.y, HEAT_ICON.width, HEAT_ICON.height, mouseX, mouseY)) {
            String key = "container.cooking_pot." +
                    (((ISuperWithoutLevelHeatable) this.menu).isSuperHeated() ? "super_heated"
                            : (this.menu.isHeated() ? "heated" : "not_heated"));

            gui.renderTooltip(this.font, TextUtils.getTranslation(key), mouseX, mouseY);
            ci.cancel();
        }
    }

    // renderBg is inherited from AbstractContainerScreen (vanilla MC, SRG m_7286_), so
    // remap = true is REQUIRED to override the class-level remap = false above.
    @Inject(method = "renderBg", at = @At("HEAD"), cancellable = true, remap = true)
    protected void farmers_heaters$renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            gui.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

            if (((ISuperWithoutLevelHeatable) this.menu).isSuperHeated()) {
                gui.blit(SUPER_HEATED_BACKGROUND_TEXTURE, this.leftPos + HEAT_ICON.x, this.topPos + HEAT_ICON.y, 193, 0, HEAT_ICON.width, HEAT_ICON.height);
            } else if (this.menu.isHeated()) {
                gui.blit(BACKGROUND_TEXTURE, this.leftPos + HEAT_ICON.x, this.topPos + HEAT_ICON.y, 176, 0, HEAT_ICON.width, HEAT_ICON.height);
            }

            int l = this.menu.getCookProgressionScaled();
            gui.blit(BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW.x, this.topPos + PROGRESS_ARROW.y, 176, 15, l + 1, PROGRESS_ARROW.height);
        }
        ci.cancel();
    }
}
