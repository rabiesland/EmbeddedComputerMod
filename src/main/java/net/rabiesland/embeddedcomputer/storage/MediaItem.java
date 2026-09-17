/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.storage;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.WritableMount;
import dan200.computercraft.api.media.IMedia;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import net.rabiesland.embeddedcomputer.registry;

import java.util.List;

public abstract class MediaItem extends Item implements IMedia {
    public MediaItem(Settings settings) {
        super(settings);
    }
    private static final String nbtId = "id";
    public void setId(ItemStack stack,int diskId1) {
        stack.set(registry.id,diskId1);
    }
    public int getId(ItemStack stack) {
        Integer diskId = stack.get(registry.id);
        if (diskId == null) return -1;
        return diskId;
    }

    public abstract int getMaxStorage();

    public abstract String getMountName();

    @Nullable
    @Override
    public String getLabel(RegistryWrapper.WrapperLookup a, ItemStack stack) {
        if (stack.get(DataComponentTypes.CUSTOM_NAME) != null) return stack.get(DataComponentTypes.CUSTOM_NAME).getString();
        else return null;
    }

    @Override
    public boolean setLabel(ItemStack stack, @Nullable String label) {
        if (label != null) stack.set(DataComponentTypes.CUSTOM_NAME,Text.of(label));
        else stack.set(DataComponentTypes.CUSTOM_NAME,null);
        return true;
    }

    @Nullable
    @Override
    public WritableMount createDataMount(ItemStack stack, ServerWorld world) {
        var diskID = getId(stack);
        if (diskID < 0) {
            diskID = ComputerCraftAPI.createUniqueNumberedSaveDir(world.getServer(), getMountName());
            setId(stack, diskID);
        }
        return ComputerCraftAPI.createSaveDirMount(world.getServer(), getMountName()+"/" + diskID, getMaxStorage());
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        var id = getId(stack);
        if (id >= 0) {
            tooltip.add(Text.literal("Id: "+id)
                    .formatted(Formatting.GRAY));
        }
    }
}