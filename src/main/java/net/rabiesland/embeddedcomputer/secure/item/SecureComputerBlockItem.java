package net.rabiesland.embeddedcomputer.secure.item;

import net.minecraft.world.item.Item;
import net.rabiesland.embeddedcomputer.embedded.item.ComputerBlockItem;
import net.rabiesland.embeddedcomputer.registry;

public class SecureComputerBlockItem extends ComputerBlockItem {
    public SecureComputerBlockItem(Item.Properties s) {
        super(registry.SECURE_COMPUTER, s);
    }
}
