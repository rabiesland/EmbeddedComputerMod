/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer;

import com.mojang.serialization.Codec;
import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
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

import java.util.function.UnaryOperator;

public class registry {
    public static Block EMBEDDED_COMPUTER = Registry.register(
            BuiltInRegistries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("embeddedcomputer","embedded_computer"),
            new EmbeddedComputerBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.IGNORE).forceSolidOn())
    );
    public static BlockEntityType<EmbeddedComputerBlockEntity> EMBEDDED_COMPUTER_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath("embeddedcomputer", "embedded_computer_entity"),
            BlockEntityType.Builder.of(EmbeddedComputerBlockEntity::new, EMBEDDED_COMPUTER).build(null)
    );
    public static Item EMBEDDED_COMPUTER_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath("embeddedcomputer", "embedded_computer"),
            new ComputerBlockItem(EMBEDDED_COMPUTER)
    );

    public static final Block HARD_DRIVE = Registry.register(
            BuiltInRegistries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("embeddedcomputer","hard_drive"),
            new HardDriveBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.IGNORE).forceSolidOn())
    );
    public static final BlockEntityType HARD_DRIVE_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,ResourceLocation.tryBuild("embeddedcomputer","hard_drive_entity"), BlockEntityType.Builder.of(HardDriveBlockEntity::new,HARD_DRIVE).build(null));
    public static Item HARD_DRIVE_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath("embeddedcomputer", "hard_drive"),
            new HardDriveItem(HARD_DRIVE, new Item.Properties().stacksTo(1))
    );

    public static Block SECURE_COMPUTER = Registry.register(
            BuiltInRegistries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("embeddedcomputer","secure_computer"),
            new SecureComputerBlock<>(BlockBehaviour.Properties.of().pushReaction(PushReaction.IGNORE).forceSolidOn())
    );
    public static BlockEntityType<SecureComputerBlockEntity> SECURE_COMPUTER_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath("embeddedcomputer", "secure_computer_entity"),
            BlockEntityType.Builder.of(SecureComputerBlockEntity::new, SECURE_COMPUTER).build(null)
    );
    public static Item SECURE_COMPUTER_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath("embeddedcomputer", "secure_computer"),
            new ComputerBlockItem(SECURE_COMPUTER)
    );

    public static final Item DEBUG_MEDIA_ITEM = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.tryBuild("embeddedcomputer", "debug_rock"), new DebugMediaItem(new Item.Properties()));
    public static final Item ZIP_DISK_ITEM = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.tryBuild("embeddedcomputer", "zip_disk"), new ZipDiskItem(new Item.Properties()));
    public static final Item FLASH_CARD_ITEM = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.tryBuild("embeddedcomputer", "flash_card"), new FlashCardItem(new Item.Properties()));

    public static final ComputerComponent<IEmbeddedComputer> EMBEDDED_COMPONENT = ComputerComponent.create("embeddedcomputer", "embedded");
    public static final ComputerComponent<IEmbeddedComputer> SECURE_COMPONENT = ComputerComponent.create("embeddedcomputer","secure");


    public static final DataComponentType<Integer> id = register("id", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT));

    public static final DataComponentType<String> uuid = register("uuid", builder -> builder
            .persistent(Codec.STRING)
            .networkSynchronized(ByteBufCodecs.STRING_UTF8));

    public static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath("embeddedcomputer", path), builderOperator.apply(DataComponentType.builder()).build());
    }

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
