/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.storage.items;

import net.rabiesland.embeddedcomputer.storage.MediaItem;
import net.rabiesland.embeddedcomputer.storage.ServerStorageConfig;

public class ZipDiskItem extends MediaItem {
    public ZipDiskItem(Settings settings) {
        super(settings);
    }
    @Override
    public int getMaxStorage() {
        return ServerStorageConfig.ZIP_DISK_STORAGE; // 5 Megabytes
    }
    @Override
    public String getMountName() {
        return "zipdisk";
    }
}