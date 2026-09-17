package net.rabiesland.embeddedcomputer.secure;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import net.rabiesland.embeddedcomputer.embedded.EmbeddedComputerPeripheral;
import net.rabiesland.embeddedcomputer.secure.block.SecureComputerBlockEntity;

public class SecureComputerPeripheral extends EmbeddedComputerPeripheral implements IPeripheral {
    public SecureComputerPeripheral(AbstractComputerBlockEntity owner) {
        super(owner);
    }
    public static IPeripheral getPeripheral(BlockEntity blockEntity, Direction direction) {
        return ((SecureComputerBlockEntity)blockEntity).peripheral();
    }
}
