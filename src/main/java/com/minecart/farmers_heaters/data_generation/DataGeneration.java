package com.minecart.farmers_heaters.data_generation;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class DataGeneration {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput output = dataGenerator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookUp = event.getLookupProvider();

        GenBlockTag blockTag = new GenBlockTag(output, lookUp, fileHelper);
        dataGenerator.addProvider(event.includeServer(), blockTag);
        dataGenerator.addProvider(event.includeServer(), new GenItemTag(output, lookUp, blockTag.contentsGetter()));

        dataGenerator.addProvider(event.includeClient(), new GenItemModel(output, fileHelper));
    }
}
