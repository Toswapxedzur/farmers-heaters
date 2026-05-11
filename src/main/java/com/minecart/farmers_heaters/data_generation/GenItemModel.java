package com.minecart.farmers_heaters.data_generation;

import com.minecart.farmers_heaters.AddonItems;
import com.minecart.farmers_heaters.FarmersHeaters;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class GenItemModel extends ItemModelProvider {
    public GenItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FarmersHeaters.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldItem(AddonItems.STURDY_KNIFE);
    }

    // 1.20.1: Helper method to replicate the handheldItem behavior for tools/weapons
    private void handheldItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(), mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/" + item.getId().getPath()));
    }
}