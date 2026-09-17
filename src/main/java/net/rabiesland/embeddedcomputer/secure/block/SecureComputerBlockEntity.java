package net.rabiesland.embeddedcomputer.secure.block;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.TerminalSize;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.rabiesland.embeddedcomputer.embedded.ServerEmbeddedComputer;
import net.rabiesland.embeddedcomputer.embedded.block.EmbeddedComputerBlockEntity;
import net.rabiesland.embeddedcomputer.registry;

import static java.util.Objects.isNull;

public class SecureComputerBlockEntity extends EmbeddedComputerBlockEntity {
    public SecureComputerBlockEntity(BlockPos pos, BlockState state) {
        super(registry.SECURE_COMPUTER_ENTITY,pos, state);
    }
    @Override
    protected ServerComputer createComputer(int id) {
        return new ServerEmbeddedComputer(
                (ServerLevel) getLevel(), getBlockPos(), //id, label,brain
                ServerEmbeddedComputer.properties(id, ComputerFamily.ADVANCED)
                        .label(label)
                        .terminalSize(new TerminalSize(49,17))
                        .addComponent(registry.SECURE_COMPONENT,this.getBrain())
        );
    }

    protected boolean wasOn = false;
    @Override
    public void serverTick() {
        if (isNull(getLevel()) || getLevel().isClientSide()) {
            return; //no.
        }
        if (getComputerID() < 0) {
            return;
        }
        var comp = createServerComputer();
        var currentlyOn = comp.isOn();
        if (currentlyOn != wasOn) {
            wasOn = currentlyOn;
            setChanged();
        }
        if (!currentlyOn) {
            comp.turnOn();
        }
        comp.keepAlive();
        updateBlockState(comp.getState());
    }
}
