package com.minecart.farmers_heaters.data_generation;

import com.minecart.farmers_heaters.AddonItems;
import com.minecart.farmers_heaters.FarmersHeaters;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class GenItemModel extends ItemModelProvider {
    public GenItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FarmersHeaters.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldItem(AddonItems.STURDY_KNIFE.get());
    }
}
