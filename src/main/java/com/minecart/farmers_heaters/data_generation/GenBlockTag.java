package com.minecart.farmers_heaters.data_generation;

import com.minecart.farmers_heaters.FarmersHeaters;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public class GenBlockTag extends BlockTagsProvider {
    public GenBlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FarmersHeaters.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 1.20.1: We pass the ResourceLocation (ID) to remove it from the tag
        tag(ModTags.HEAT_SOURCES).remove(ModBlocks.STOVE.getId());
    }
}