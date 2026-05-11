package com.minecart.farmers_heaters;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.KnifeItem;

public class AddonItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmersHeaters.MODID);

    // 1.20.1: Returns a RegistryObject instead of a raw Supplier
    public static final RegistryObject<Item> STURDY_KNIFE = ITEMS.register("sturdy_knife",
            () -> new KnifeItem(AllBlockItem.STURDY, 1, 1, new Item.Properties().fireResistant())
    );
}
