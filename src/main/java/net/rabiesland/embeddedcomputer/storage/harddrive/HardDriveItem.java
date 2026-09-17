package net.rabiesland.embeddedcomputer.storage.harddrive;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HardDriveItem  extends BlockItem {
    public HardDriveItem(Block block, Settings settings) {
        super(block, settings);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt == null) return;
        if (!nbt.contains("uuid")) return;
        if (nbt.getString("uuid").isEmpty()) return;
        tooltip.add(Text.literal("Drive: "+nbt.getString("uuid")).formatted(Formatting.DARK_GRAY));
    }
}
