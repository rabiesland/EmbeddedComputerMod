package net.rabiesland.embeddedcomputer.secure.block;

import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.platform.PlatformHelper;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.rabiesland.embeddedcomputer.embedded.block.EmbeddedComputerBlock;
import net.rabiesland.embeddedcomputer.embedded.block.EmbeddedComputerBlockEntity;
import net.rabiesland.embeddedcomputer.embedded.item.ComputerBlockItem;
import net.rabiesland.embeddedcomputer.registry;

import static java.util.Objects.isNull;

public class SecureComputerBlock<T extends SecureComputerBlockEntity> extends EmbeddedComputerBlock {
    public SecureComputerBlock(Settings settings) {
        super(settings);
    }

    private final BlockEntityTicker<T> ticker = (level, pos, state, computer) -> computer.serverTick();
    @Override
    public BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType type) {
        return world.isClient ? null : BlockEntityHelpers.createTickerHelper(type, (BlockEntityType) registry.SECURE_COMPUTER_ENTITY, ticker);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SecureComputerBlockEntity(pos,state);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        var id = -1;
        var comp1 = world.getBlockEntity(pos);
        if (!isNull(comp1)) {
            id = ((EmbeddedComputerBlockEntity) comp1).getComputerID();
        }
        return ((ComputerBlockItem)registry.SECURE_COMPUTER_ITEM).newComputerItem(id);
    }

    protected ItemStack getItem(SecureComputerBlockEntity be) {
        if (!(asItem() instanceof ComputerBlockItem)) {
            return ItemStack.EMPTY;
        }
        return ((ComputerBlockItem)registry.SECURE_COMPUTER_ITEM).newComputerItem(be.getComputerID());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        var blockEntity1 = world.getBlockEntity(pos);
        if (!world.isClient() && !isNull(blockEntity1)) {
            var blockEntity = (SecureComputerBlockEntity) blockEntity1;
            var computer = blockEntity.getServerComputer();
            if (isNull(computer)) {
                computer = blockEntity.createServerComputer();
            }
            if (computer.checkUsable(player)) {
                PlatformHelper.get().openMenu(player,blockEntity.getName(),blockEntity, new ComputerContainerData(computer,getItem(blockEntity)));
            }
        }
        return ActionResult.success(true);
    }
}
