/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.embedded;

import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class ServerEmbeddedComputer extends ServerComputer {
    public ServerEmbeddedComputer(ServerWorld level, BlockPos position, Properties properties) {
        super(level, position, properties);
    }
}
