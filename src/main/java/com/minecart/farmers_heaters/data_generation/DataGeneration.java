package com.minecart.farmers_heaters.data_generation;

import com.minecart.farmers_heaters.FarmersHeaters;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

// 1.20.1: Explicitly define the MOD bus and your MODID
@Mod.EventBusSubscriber(modid = FarmersHeaters.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput output = dataGenerator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookUp = event.getLookupProvider();

        GenBlockTag blockTag = new GenBlockTag(output, lookUp, fileHelper);
        dataGenerator.addProvider(event.includeServer(), blockTag);
        dataGenerator.addProvider(event.includeServer(),
                new GenItemTag(output, lookUp, blockTag.contentsGetter(), fileHelper));

        dataGenerator.addProvider(event.includeClient(), new GenItemModel(output, fileHelper));
    }
}
