/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.storage;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.WritableMount;
import dan200.computercraft.api.media.IMedia;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MediaItem extends Item implements IMedia {
    public MediaItem(Settings settings) {
        super(settings);
    }
    private static final String nbtId = "id";
    public void setId(ItemStack stack,int diskId1) {
        var nbt = stack.getOrCreateNbt();
        nbt.putInt(nbtId,diskId1);
    }
    public int getId(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains(nbtId)) return -1;
        return nbt.getInt(nbtId);
    }

    public abstract int getMaxStorage();

    public abstract String getMountName();

    @Nullable
    @Override
    public String getLabel(ItemStack stack) {
        if (stack.hasCustomName()) return stack.getName().getString();
        else return null;
    }

    @Override
    public boolean setLabel(ItemStack stack, @Nullable String label) {
        if (label != null) stack.setCustomName(Text.of(label));
        else stack.removeCustomName();
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext options) {
        var id = getId(stack);
        if (id >= 0) {
            tooltip.add(Text.literal("Id: "+id)
                    .formatted(Formatting.GRAY));
        }
    }
}