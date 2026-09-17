/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.storage;

import net.rabiesland.embeddedcomputer.Config;

public class ServerStorageConfig {
    public static int ZIP_DISK_STORAGE = 0;
    public static int FLASH_CARD_STORAGE = 0;
    public static int DEBUG_ITEM_STORAGE = 0;
    public static int HARD_DRIVE_STORAGE = 0;

    public static void updateConfig() {
        ZIP_DISK_STORAGE = Config.HANDLER.instance().ZIP_DISK_STORAGE;
        FLASH_CARD_STORAGE = Config.HANDLER.instance().FLASH_CARD_STORAGE;
        DEBUG_ITEM_STORAGE = Config.HANDLER.instance().DEBUG_ITEM_STORAGE;
        HARD_DRIVE_STORAGE = Config.HANDLER.instance().HARD_DRIVE_STORAGE;
    }
}