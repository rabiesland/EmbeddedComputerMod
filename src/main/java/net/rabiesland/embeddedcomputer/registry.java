/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer;

import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.api.peripheral.PeripheralLookup;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.Identifier;

import net.rabiesland.embeddedcomputer.embedded.EmbeddedComputerPeripheral;
import net.rabiesland.embeddedcomputer.embedded.IEmbeddedComputer;
import net.rabiesland.embeddedcomputer.embedded.block.EmbeddedComputerBlock;
import net.rabiesland.embeddedcomputer.embedded.block.EmbeddedComputerBlockEntity;
import net.rabiesland.embeddedcomputer.embedded.item.ComputerBlockItem;
import net.rabiesland.embeddedcomputer.secure.SecureComputerPeripheral;
import net.rabiesland.embeddedcomputer.secure.block.SecureComputerBlock;
import net.rabiesland.embeddedcomputer.secure.block.SecureComputerBlockEntity;
import net.rabiesland.embeddedcomputer.storage.harddrive.HardDriveBlock;
import net.rabiesland.embeddedcomputer.storage.harddrive.HardDriveBlockEntity;
import net.rabiesland.embeddedcomputer.platform.registry1;
import net.rabiesland.embeddedcomputer.storage.harddrive.HardDriveItem;
import net.rabiesland.embeddedcomputer.storage.harddrive.HardDrivePeripheral;
import net.rabiesland.embeddedcomputer.storage.items.DebugMediaItem;
import net.rabiesland.embeddedcomputer.storage.items.FlashCardItem;
import net.rabiesland.embeddedcomputer.storage.items.ZipDiskItem;

public class registry {
    public static Block EMBEDDED_COMPUTER = Registry.register(
            Registries.BLOCK,
            new Identifier("embeddedcomputer","embedded_computer"),
            new EmbeddedComputerBlock(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.IGNORE).solid())
    );
    public static BlockEntityType<EmbeddedComputerBlockEntity> EMBEDDED_COMPUTER_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier("embeddedcomputer", "embedded_computer_entity"),
            BlockEntityType.Builder.create(EmbeddedComputerBlockEntity::new, EMBEDDED_COMPUTER).build(null)
    );
    public static Item EMBEDDED_COMPUTER_ITEM = Registry.register(
            Registries.ITEM,
            new Identifier("embeddedcomputer", "embedded_computer"),
            new ComputerBlockItem(EMBEDDED_COMPUTER)
    );

    public static final Block HARD_DRIVE = Registry.register(
            Registries.BLOCK,
            new Identifier("embeddedcomputer","hard_drive"),
            new HardDriveBlock(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.IGNORE).solid())
    );
    public static final BlockEntityType HARD_DRIVE_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,Identifier.of("embeddedcomputer","hard_drive_entity"), BlockEntityType.Builder.create(HardDriveBlockEntity::new,HARD_DRIVE).build(null));
    public static Item HARD_DRIVE_ITEM = Registry.register(
            Registries.ITEM,
            new Identifier("embeddedcomputer", "hard_drive"),
            new HardDriveItem(HARD_DRIVE, new FabricItemSettings().maxCount(1))
    );

    public static Block SECURE_COMPUTER = Registry.register(
            Registries.BLOCK,
            new Identifier("embeddedcomputer","secure_computer"),
            new SecureComputerBlock<>(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.IGNORE).solid())
    );
    public static BlockEntityType<SecureComputerBlockEntity> SECURE_COMPUTER_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier("embeddedcomputer", "secure_computer_entity"),
            BlockEntityType.Builder.create(SecureComputerBlockEntity::new, SECURE_COMPUTER).build(null)
    );
    public static Item SECURE_COMPUTER_ITEM = Registry.register(
            Registries.ITEM,
            new Identifier("embeddedcomputer", "secure_computer"),
            new ComputerBlockItem(SECURE_COMPUTER)
    );

    public static final Item DEBUG_MEDIA_ITEM = Registry.register(Registries.ITEM, Identifier.of("embeddedcomputer", "debug_rock"), new DebugMediaItem(new Item.Settings()));
    public static final Item ZIP_DISK_ITEM = Registry.register(Registries.ITEM, Identifier.of("embeddedcomputer", "zip_disk"), new ZipDiskItem(new Item.Settings()));
    public static final Item FLASH_CARD_ITEM = Registry.register(Registries.ITEM, Identifier.of("embeddedcomputer", "flash_card"), new FlashCardItem(new Item.Settings()));

    public static final ComputerComponent<IEmbeddedComputer> EMBEDDED_COMPONENT = ComputerComponent.create("embeddedcomputer", "embedded");
    public static final ComputerComponent<IEmbeddedComputer> SECURE_COMPONENT = ComputerComponent.create("embeddedcomputer","secure");

    public void registerPeripherals() {
        PeripheralLookup.get().registerForBlockEntities(EmbeddedComputerPeripheral::getPeripheral,EMBEDDED_COMPUTER_ENTITY);
        PeripheralLookup.get().registerForBlockEntities(HardDrivePeripheral::getPeripheral,HARD_DRIVE_ENTITY);
        PeripheralLookup.get().registerForBlockEntities(SecureComputerPeripheral::getPeripheral,SECURE_COMPUTER_ENTITY);
    }
    public void registerItemGroups() {
        var a = new registry1();
        a.registerItemGroups();
    }
}
