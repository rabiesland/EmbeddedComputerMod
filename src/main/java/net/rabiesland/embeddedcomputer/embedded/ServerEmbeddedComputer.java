/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.embedded;

import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class ServerEmbeddedComputer extends ServerComputer {
    public ServerEmbeddedComputer(ServerLevel level, BlockPos position, Properties properties) {
        super(level, position, properties);
    }
}
