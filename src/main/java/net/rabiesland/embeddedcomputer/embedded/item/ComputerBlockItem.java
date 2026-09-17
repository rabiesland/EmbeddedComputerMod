/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.embedded.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ComputerBlockItem extends BlockItem {
    public ComputerBlockItem(Block block) {
        super(block, new Item.Settings().fireproof());
    }
    public ItemStack newComputerItem(int id) {
        var stack = new ItemStack(this);
        if (id > 0) stack.getOrCreateNbt().putInt("ComputerId",id);
        return stack;
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt == null) return;
        if (nbt.getInt("ComputerId") < 0) return;
        tooltip.add(Text.literal("Computer: "+nbt.getInt("ComputerId")).formatted(Formatting.DARK_GRAY));
    }
}
