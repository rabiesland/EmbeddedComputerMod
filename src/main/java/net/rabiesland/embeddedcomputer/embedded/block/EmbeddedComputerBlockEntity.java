/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.embedded.block;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.TerminalSize;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.rabiesland.embeddedcomputer.embedded.EmbeddedComputerBrain;
import net.rabiesland.embeddedcomputer.embedded.EmbeddedComputerPeripheral;
import net.rabiesland.embeddedcomputer.embedded.ServerEmbeddedComputer;
import net.rabiesland.embeddedcomputer.registry;

import static java.util.Objects.isNull;

public class EmbeddedComputerBlockEntity extends ComputerBlockEntity {
    private IPeripheral p;
    public EmbeddedComputerBlockEntity(BlockEntityType type, BlockPos pos, BlockState state) {
        super(type,pos,state,ComputerFamily.ADVANCED);
    }
    public EmbeddedComputerBlockEntity(BlockPos pos, BlockState state) {
        super(registry.EMBEDDED_COMPUTER_ENTITY,pos,state,ComputerFamily.ADVANCED);
    }
    private final EmbeddedComputerBrain brain = new EmbeddedComputerBrain(this); // This does nothing. it's just there to make the Computer Component happy lmao
    public EmbeddedComputerBrain getBrain() {
        return brain;
    }
    @Override
    protected ServerComputer createComputer(int id) {
        return new ServerEmbeddedComputer(
                (ServerWorld) getWorld(), getPos(), //id, label,brain
                ServerEmbeddedComputer.properties(id,ComputerFamily.ADVANCED)
                        .label(label)
                        .terminalSize(new TerminalSize(10,3))
                        .addComponent(registry.SECURE_COMPONENT,brain)
                        .addComponent(registry.EMBEDDED_COMPONENT,brain)
        );
    }
    @Override
    public IPeripheral peripheral() {
        if (!isNull(p)){
            return p;
        }
        return p = new EmbeddedComputerPeripheral(this);
    }
    protected boolean wasOn = false;
    @Override
    public void serverTick() {
        if (isNull(getWorld()) || getWorld().isClient) {
            return; //no.
        }
        if (getComputerID() < 0) {
            return;
        }
        var comp = createServerComputer();
        var currentlyOn = comp.isOn();
        if (currentlyOn != wasOn) {
            wasOn = currentlyOn;
            markDirty();
        }
        if (!currentlyOn) {
            comp.turnOn();
        }
        comp.keepAlive();
        updateBlockState(comp.getState());
    }
}
