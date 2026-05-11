package com.minecart.farmers_heaters.data_generation;

import com.minecart.farmers_heaters.AddonTags;
import com.minecart.farmers_heaters.FarmersHeaters;
import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class GenItemTag extends ItemTagsProvider {
    public GenItemTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
//        tag(AddonTags.Items.NETHER_FOOD).add(MNDItems.SAUSAGE_AND_POTATOES.get(), MNDItems.FRIED_HOGLIN_CHOP.get(), MNDItems.ROAST_STUFFED_HOGLIN.get(), MNDItems.DEVILED_EGG.get(), MNDItems.SCOTCH_EGGS.get(), MNDItems.EGG_SOUP.get(), MNDItems.STRIDER_WITH_GRILLED_FUNGUS.get(), MNDItems.STRIDER_STEW.get(), MNDItems.CRIMSON_STROGANOFF.get(), MNDItems.TWISTED_GHASTA.get(), MNDItems.SPICY_NOODLE_SOUP.get(), MNDItems.FRIES_GHASTA.get(), MNDItems.GIANT_TAKOYAKI.get(), MNDItems.CHILIDOG.get(), MNDItems.SPICY_HOGLIN_STEW.get(), MNDItems.HOT_WINGS.get(), MNDItems.SPICY_CURRY.get(), MNDItems.ROCK_SOUP.get(), MNDItems.BURNT_ROLL.get(), MNDItems.HOT_CREAM.get());
    }
}
