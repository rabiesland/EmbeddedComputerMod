package net.rabiesland.embeddedcomputer.storage.harddrive;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.rabiesland.embeddedcomputer.registry;

import java.util.List;

public class HardDriveItem  extends BlockItem {
    public HardDriveItem(Block block, Settings settings) {
        super(block, settings);
    }
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        String uuid = stack.get(registry.uuid);
        if (uuid == null) return;
        if (uuid.isEmpty()) return;
        tooltip.add(Text.literal("Drive: "+uuid).formatted(Formatting.DARK_GRAY));
    }
}
