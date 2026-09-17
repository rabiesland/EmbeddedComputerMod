/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.embedded.item;

import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.util.NonNegativeId;
import net.rabiesland.embeddedcomputer.registry;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class ComputerBlockItem extends BlockItem {
    public ComputerBlockItem(Block block, Item.Properties s) {
        super(block, s);
    }
    public ComputerBlockItem(Block block) {
        this(block, new Item.Properties().fireResistant());
    }
    public ComputerBlockItem(Item.Properties s) {
        this(registry.EMBEDDED_COMPUTER, s);
    }
    public ItemStack newComputerItem(int id) {
        var stack = new ItemStack(this);
        if (id > 0) stack.set(ModRegistry.DataComponents.COMPUTER_ID.get(), new NonNegativeId.Computer(id));
        return stack;
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        NonNegativeId computerId = stack.get(ModRegistry.DataComponents.COMPUTER_ID.get());
        if (computerId == null) return;
        if (computerId.id() < 0) return;
        textConsumer.accept(Component.literal("Computer: "+computerId.id()).withStyle(ChatFormatting.DARK_GRAY));
    }
}
