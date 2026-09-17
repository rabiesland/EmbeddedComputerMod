package net.rabiesland.embeddedcomputer.storage.harddrive;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.rabiesland.embeddedcomputer.registry;

import java.util.List;

public class HardDriveItem  extends BlockItem {
    public HardDriveItem(Block block, Properties settings) {
        super(block, settings);
    }
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
        String uuid = stack.get(registry.uuid);
        if (uuid == null) return;
        if (uuid.isEmpty()) return;
        tooltip.add(Component.literal("Drive: "+uuid).withStyle(ChatFormatting.DARK_GRAY));
    }
}
