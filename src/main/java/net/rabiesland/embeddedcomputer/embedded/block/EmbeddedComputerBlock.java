/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.embedded.block;

import dan200.computercraft.shared.computer.blocks.ComputerBlock;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import net.rabiesland.embeddedcomputer.embedded.item.ComputerBlockItem;
import net.rabiesland.embeddedcomputer.registry;

import static java.util.Objects.isNull;

public class EmbeddedComputerBlock<T extends EmbeddedComputerBlockEntity> extends HorizontalDirectionalBlock  implements EntityBlock {
    public static EnumProperty powered = ComputerBlock.STATE;

    public EmbeddedComputerBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(powered,ComputerState.OFF));
    }

    private final BlockEntityTicker<T> ticker = (level, pos, state, computer) -> computer.serverTick();
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING,powered);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EmbeddedComputerBlockEntity(pos,state);
    }
    // update for peripherals
    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        var comp1 = world.getBlockEntity(pos);
        if (!isNull(comp1) && comp1 instanceof EmbeddedComputerBlockEntity) {
            var comp = (EmbeddedComputerBlockEntity) comp1;
            comp.neighborChanged();
        }
    }
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        var comp1 = world.getBlockEntity(pos);
        if (!isNull(comp1) && comp1 instanceof EmbeddedComputerBlockEntity) {
            var comp = (EmbeddedComputerBlockEntity) comp1;
            comp.updateInputsImmediately();
        }
    }
    //turn on computer
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        var blockEntity1 = world.getBlockEntity(pos);
        if (!world.isClientSide() && !isNull(blockEntity1)) {
            var blockEntity = (EmbeddedComputerBlockEntity) blockEntity1;
            var computer = blockEntity.getServerComputer();
            if (isNull(computer)) {
                computer = blockEntity.createServerComputer();
                computer.turnOn();
            }
            else {
                computer.reboot();
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }
    @Override
    public BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType type) {
        return world.isClientSide() ? null : BlockEntityHelpers.createTickerHelper(type, (BlockEntityType) registry.EMBEDDED_COMPUTER_ENTITY, ticker);
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        var id = -1;
        var comp1 = world.getBlockEntity(pos);
        if (!isNull(comp1)) {
            id = ((EmbeddedComputerBlockEntity) comp1).getComputerID();
        }
        return ((ComputerBlockItem)registry.EMBEDDED_COMPUTER_ITEM).newComputerItem(id);
    }

    public Item asItem() {
        return registry.EMBEDDED_COMPUTER_ITEM;
    }

}
