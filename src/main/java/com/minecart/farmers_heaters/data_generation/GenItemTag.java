package com.minecart.farmers_heaters.data_generation;

import com.minecart.farmers_heaters.FarmersHeaters;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

// 1.20.1 Forge equivalent of the 1.21.1 NeoForge GenItemTag.
// Currently empty; the farmers_heaters:nether_food tag is provided as a hand-written
// resource at data/farmers_heaters/tags/items/nether_food.json (with a forge mod_loaded
// condition on mynethersdelight). This class exists for parity and as a stub for future
// data-gen entries.
public class GenItemTag extends ItemTagsProvider {
    public GenItemTag(PackOutput output,
                      CompletableFuture<HolderLookup.Provider> lookupProvider,
                      CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTags,
                      ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, FarmersHeaters.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Intentionally empty. Hand-written nether_food.json provides the conditional list.
//        tag(AddonTags.Items.NETHER_FOOD).add(
//            MNDItems.SAUSAGE_AND_POTATOES.get(), MNDItems.FRIED_HOGLIN_CHOP.get(), ...
//        );
    }
}
