package net.rabiesland.embeddedcomputer.storage.harddrive;

import net.rabiesland.embeddedcomputer.registry;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class HardDriveItem  extends BlockItem {
    public HardDriveItem(net.minecraft.world.item.Item.Properties settings) {
        super(registry.HARD_DRIVE, settings);
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        String uuid = stack.get(registry.uuid);
        if (uuid == null) return;
        if (uuid.isEmpty()) return;
        textConsumer.accept(Component.literal("Drive: "+uuid).withStyle(ChatFormatting.DARK_GRAY));
    }
}
