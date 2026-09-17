/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.rabiesland.embeddedcomputer.registry;

public class registry1 {
    public static final ItemGroup EMBEDDED_COMPUTER_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(registry.EMBEDDED_COMPUTER))
            .displayName(Text.translatable("itemGroup.embeddedcomputer"))
            .build();
    public static RegistryKey<ItemGroup> itemGroupKey = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of("embeddedcomputer", "item_group"));
    public void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP,itemGroupKey, EMBEDDED_COMPUTER_GROUP);
        ItemGroupEvents.modifyEntriesEvent(itemGroupKey).register(itemGroup -> {
            itemGroup.add(registry.EMBEDDED_COMPUTER_ITEM);
            itemGroup.add(registry.SECURE_COMPUTER_ITEM);
            itemGroup.add(registry.HARD_DRIVE_ITEM);
            itemGroup.add(registry.DEBUG_MEDIA_ITEM);
            itemGroup.add(registry.ZIP_DISK_ITEM);
            itemGroup.add(registry.FLASH_CARD_ITEM);
        });
    }
}
