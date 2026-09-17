/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer;

import com.mojang.serialization.Codec;
import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.api.peripheral.PeripheralLookup;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import net.rabiesland.embeddedcomputer.secure.item.SecureComputerBlockItem;
import net.rabiesland.embeddedcomputer.storage.harddrive.HardDriveBlock;
import net.rabiesland.embeddedcomputer.storage.harddrive.HardDriveBlockEntity;
import net.rabiesland.embeddedcomputer.platform.registry1;
import net.rabiesland.embeddedcomputer.storage.harddrive.HardDriveItem;
import net.rabiesland.embeddedcomputer.storage.harddrive.HardDrivePeripheral;
import net.rabiesland.embeddedcomputer.storage.items.DebugMediaItem;
import net.rabiesland.embeddedcomputer.storage.items.FlashCardItem;
import net.rabiesland.embeddedcomputer.storage.items.ZipDiskItem;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class registry {
    public static Block EMBEDDED_COMPUTER = registerBlock("embedded_computer", EmbeddedComputerBlock::new, BlockBehaviour.Properties.of().pushReaction(PushReaction.IGNORE).forceSolidOn());
    public static BlockEntityType<EmbeddedComputerBlockEntity> EMBEDDED_COMPUTER_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("embeddedcomputer", "embedded_computer_entity"),
            FabricBlockEntityTypeBuilder.create(EmbeddedComputerBlockEntity::new, EMBEDDED_COMPUTER).build(null)
    );
    public static Item EMBEDDED_COMPUTER_ITEM = registerItem("embedded_computer", ComputerBlockItem::new, new Item.Properties().fireResistant());

    public static final Block HARD_DRIVE = registerBlock("hard_drive",HardDriveBlock::new,BlockBehaviour.Properties.of().pushReaction(PushReaction.IGNORE).forceSolidOn());
    public static final BlockEntityType HARD_DRIVE_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,Identifier.tryBuild("embeddedcomputer","hard_drive_entity"), FabricBlockEntityTypeBuilder.create(HardDriveBlockEntity::new,HARD_DRIVE).build(null));
    public static Item HARD_DRIVE_ITEM = registerItem("hard_drive",HardDriveItem::new,new Item.Properties().stacksTo(1));

    public static Block SECURE_COMPUTER = registerBlock("secure_computer", SecureComputerBlock::new, BlockBehaviour.Properties.of().pushReaction(PushReaction.IGNORE).forceSolidOn());
    public static BlockEntityType<SecureComputerBlockEntity> SECURE_COMPUTER_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("embeddedcomputer", "secure_computer_entity"),
            FabricBlockEntityTypeBuilder.create(SecureComputerBlockEntity::new, SECURE_COMPUTER).build(null)
    );
    public static Item SECURE_COMPUTER_ITEM = registerItem("secure_computer", SecureComputerBlockItem::new, new Item.Properties().fireResistant());

    public static final Item DEBUG_MEDIA_ITEM = registerItem("debug_rock", DebugMediaItem::new, new Item.Properties());
    public static final Item ZIP_DISK_ITEM = registerItem("zip_disk", ZipDiskItem::new, new Item.Properties());
    public static final Item FLASH_CARD_ITEM = registerItem("flash_card", FlashCardItem::new, new Item.Properties());

    public static final ComputerComponent<IEmbeddedComputer> EMBEDDED_COMPONENT = ComputerComponent.create("embeddedcomputer", "embedded");
    public static final ComputerComponent<IEmbeddedComputer> SECURE_COMPONENT = ComputerComponent.create("embeddedcomputer","secure");


    public static final DataComponentType<Integer> id = register("id", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT));

    public static final DataComponentType<String> uuid = register("uuid", builder -> builder
            .persistent(Codec.STRING)
            .networkSynchronized(ByteBufCodecs.STRING_UTF8));

    public static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath("embeddedcomputer", path), builderOperator.apply(DataComponentType.builder()).build());
    }

    public static Item registerItem(String path, Function<Item.Properties, Item> factory, Item.Properties settings) {
        final ResourceKey<Item> registryKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("embeddedcomputer", path));
        return Items.registerItem(registryKey, factory, settings);
    }
    private static Block registerBlock(String path, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        final Identifier identifier = Identifier.fromNamespaceAndPath("embeddedcomputer", path);
        final ResourceKey<Block> registryKey = ResourceKey.create(Registries.BLOCK, identifier);

        return Blocks.register(registryKey, factory, settings);
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
