/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.embedded.item;

import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.util.NonNegativeId;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public class ComputerBlockItem extends BlockItem {
    public ComputerBlockItem(Block block) {
        super(block, new Item.Properties().fireResistant());
    }
    public ItemStack newComputerItem(int id) {
        var stack = new ItemStack(this);
        if (id > 0) stack.set(ModRegistry.DataComponents.COMPUTER_ID.get(),NonNegativeId.of(id));
        return stack;
    }
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
        NonNegativeId computerId = stack.get(ModRegistry.DataComponents.COMPUTER_ID.get());
        if (computerId == null) return;
        if (computerId.id() < 0) return;
        tooltip.add(Component.literal("Computer: "+computerId.id()).withStyle(ChatFormatting.DARK_GRAY));
    }
}
