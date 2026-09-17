/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.embedded.block;

import com.mojang.serialization.MapCodec;
import dan200.computercraft.shared.computer.blocks.ComputerBlock;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.world.WorldView;
import net.rabiesland.embeddedcomputer.embedded.item.ComputerBlockItem;
import net.rabiesland.embeddedcomputer.registry;

import static java.util.Objects.isNull;

public class EmbeddedComputerBlock<T extends EmbeddedComputerBlockEntity> extends HorizontalFacingBlock  implements BlockEntityProvider {
    public static EnumProperty powered = ComputerBlock.STATE;

    public EmbeddedComputerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(powered,ComputerState.OFF));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return createCodec(EmbeddedComputerBlock::new);
    }

    private final BlockEntityTicker<T> ticker = (level, pos, state, computer) -> computer.serverTick();
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING,powered);
    }
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EmbeddedComputerBlockEntity(pos,state);
    }
    // update for peripherals
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        var comp1 = world.getBlockEntity(pos);
        if (!isNull(comp1) && comp1 instanceof EmbeddedComputerBlockEntity) {
            var comp = (EmbeddedComputerBlockEntity) comp1;
            comp.neighborChanged(pos);
        }
    }
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        var comp1 = world.getBlockEntity(pos);
        if (!isNull(comp1) && comp1 instanceof EmbeddedComputerBlockEntity) {
            var comp = (EmbeddedComputerBlockEntity) comp1;
            comp.updateInputsImmediately();
        }
    }

    //turn on computer
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        var blockEntity1 = world.getBlockEntity(pos);
        if (!world.isClient() && !isNull(blockEntity1)) {
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
        return ActionResult.success(true);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    @Override
    public BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType type) {
        return world.isClient ? null : BlockEntityHelpers.createTickerHelper(type, (BlockEntityType) registry.EMBEDDED_COMPUTER_ENTITY, ticker);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
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
