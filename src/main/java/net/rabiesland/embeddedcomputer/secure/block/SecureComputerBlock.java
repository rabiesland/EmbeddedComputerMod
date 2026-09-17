package net.rabiesland.embeddedcomputer.secure.block;

import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.platform.PlatformHelper;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.rabiesland.embeddedcomputer.embedded.block.EmbeddedComputerBlock;
import net.rabiesland.embeddedcomputer.embedded.block.EmbeddedComputerBlockEntity;
import net.rabiesland.embeddedcomputer.embedded.item.ComputerBlockItem;
import net.rabiesland.embeddedcomputer.registry;

import static java.util.Objects.isNull;

public class SecureComputerBlock<T extends SecureComputerBlockEntity> extends EmbeddedComputerBlock {
    public SecureComputerBlock(Properties settings) {
        super(settings);
    }

    private final BlockEntityTicker<T> ticker = (level, pos, state, computer) -> computer.serverTick();
    @Override
    public BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType type) {
        return world.isClientSide() ? null : BlockEntityHelpers.createTickerHelper(type, (BlockEntityType) registry.SECURE_COMPUTER_ENTITY, ticker);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SecureComputerBlockEntity(pos,state);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
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
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        var blockEntity1 = world.getBlockEntity(pos);
        if (!world.isClientSide() && !isNull(blockEntity1)) {
            var blockEntity = (SecureComputerBlockEntity) blockEntity1;
            var computer = blockEntity.getServerComputer();
            if (isNull(computer)) {
                computer = blockEntity.createServerComputer();
            }
            if (computer.checkUsable(player)) {
                PlatformHelper.get().openMenu(player,blockEntity.getName(),blockEntity, new ComputerContainerData(computer,getItem(blockEntity)));
            }
        }
        return InteractionResult.SUCCESS;
    }
}
