package com.minecart.farmers_heaters;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModItems;

public class AddonItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmersHeaters.MODID);

    public static final RegistryObject<Item> STURDY_KNIFE = ITEMS.register("sturdy_knife",
            () -> new KnifeItem(AllBlockItem.STURDY, 0.5F, -2.0F, new Item.Properties().fireResistant())
    );

    static {
        ModItems.CREATIVE_TAB_ITEMS.add(STURDY_KNIFE);
    }
}
