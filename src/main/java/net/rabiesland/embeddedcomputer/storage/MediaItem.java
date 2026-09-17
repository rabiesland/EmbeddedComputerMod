/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.storage;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.WritableMount;
import dan200.computercraft.api.media.IMedia;
import org.jetbrains.annotations.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.rabiesland.embeddedcomputer.registry;

import java.util.List;

public abstract class MediaItem extends Item implements IMedia {
    public MediaItem(Properties settings) {
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
    public String getLabel(HolderLookup.Provider a, ItemStack stack) {
        if (stack.get(DataComponents.CUSTOM_NAME) != null) return stack.get(DataComponents.CUSTOM_NAME).getString();
        else return null;
    }

    @Override
    public boolean setLabel(ItemStack stack, @Nullable String label) {
        if (label != null) stack.set(DataComponents.CUSTOM_NAME,Component.nullToEmpty(label));
        else stack.set(DataComponents.CUSTOM_NAME,null);
        return true;
    }

    @Nullable
    @Override
    public WritableMount createDataMount(ItemStack stack, ServerLevel world) {
        var diskID = getId(stack);
        if (diskID < 0) {
            diskID = ComputerCraftAPI.createUniqueNumberedSaveDir(world.getServer(), getMountName());
            setId(stack, diskID);
        }
        return ComputerCraftAPI.createSaveDirMount(world.getServer(), getMountName()+"/" + diskID, getMaxStorage());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
        var id = getId(stack);
        if (id >= 0) {
            tooltip.add(Component.literal("Id: "+id)
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}