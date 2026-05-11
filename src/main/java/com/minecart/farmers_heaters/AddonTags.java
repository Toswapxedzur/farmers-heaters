package com.minecart.farmers_heaters;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AddonTags {
    public static class Blocks {
        private static TagKey<Block> tag(String name) {
            // 1.20.1: Standard vanilla helper for Block tags
            return BlockTags.create(FarmersHeaters.modLoc(name));
        }
    }

    public static class Items {
        public static final TagKey<Item> NETHER_FOOD = tag("nether_food");

        private static TagKey<Item> tag(String name) {
            // 1.20.1: Standard vanilla helper for Item tags
            return ItemTags.create(FarmersHeaters.modLoc(name));
        }
    }
}
