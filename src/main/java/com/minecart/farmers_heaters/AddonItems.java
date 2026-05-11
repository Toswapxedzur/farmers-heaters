package com.minecart.farmers_heaters;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public class AddonItems {
    public static final DeferredRegister.Items ITEMS;

    public static final Supplier<Item> STURDY_KNIFE;

    static {
        ITEMS = DeferredRegister.createItems(FarmersHeaters.MODID);

        STURDY_KNIFE = ITEMS.register("sturdy_knife", ()->new KnifeItem(AllBlockItem.STURDY, ModItems.knifeItem(AllBlockItem.STURDY).fireResistant()));

        ModItems.CREATIVE_TAB_ITEMS.add(STURDY_KNIFE);
    }
}
