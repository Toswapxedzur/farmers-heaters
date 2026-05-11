package com.minecart.farmers_heaters;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

public class AddonTags {
    public static class Blocks{
        private static TagKey<Block> tag(String name){
            return TagKey.create(Registries.BLOCK, FarmersHeaters.modLoc(name));
        }
    }

    public static class Items{
        public static final TagKey<Item> NETHER_FOOD = tag("nether_food");

        private static TagKey<Item> tag(String name){
            return TagKey.create(Registries.ITEM, FarmersHeaters.modLoc(name));
        }
    }
}
