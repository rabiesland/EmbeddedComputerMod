/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.rabiesland.embeddedcomputer.registry;

public class registry1 {
    public static final CreativeModeTab EMBEDDED_COMPUTER_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(registry.EMBEDDED_COMPUTER))
            .title(Component.translatable("itemGroup.embeddedcomputer"))
            .build();
    public static ResourceKey<CreativeModeTab> itemGroupKey = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.tryBuild("embeddedcomputer", "item_group"));
    public void registerItemGroups() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,itemGroupKey, EMBEDDED_COMPUTER_GROUP);
        ItemGroupEvents.modifyEntriesEvent(itemGroupKey).register(itemGroup -> {
            itemGroup.accept(registry.EMBEDDED_COMPUTER_ITEM);
            itemGroup.accept(registry.SECURE_COMPUTER_ITEM);
            itemGroup.accept(registry.HARD_DRIVE_ITEM);
            itemGroup.accept(registry.DEBUG_MEDIA_ITEM);
            itemGroup.accept(registry.ZIP_DISK_ITEM);
            itemGroup.accept(registry.FLASH_CARD_ITEM);
        });
    }
}
