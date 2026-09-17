/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.storage.harddrive;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.WritableMount;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.rabiesland.embeddedcomputer.registry;
import org.jetbrains.annotations.Nullable;
import net.rabiesland.embeddedcomputer.storage.ServerStorageConfig;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import static java.util.Objects.isNull;

public class HardDriveBlockEntity extends BlockEntity  {
    public HardDriveBlockEntity(BlockPos pos, BlockState state) {
        super(registry.HARD_DRIVE_ENTITY,pos, state);
    }
    private final HardDrivePeripheral periph = new HardDrivePeripheral(this);
    public String uuid = "";
    public String mount;

    public static void tick(Level world1, BlockPos pos, BlockState state1, BlockEntity be) {}
    public WritableMount makeMount() {
        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString();
            setChanged();
        }
        return ComputerCraftAPI.createSaveDirMount(level.getServer(), "hdd/" + uuid, ServerStorageConfig.HARD_DRIVE_STORAGE); // 25 Megabytes
    }
    public boolean attach(IComputerAccess computer, @Nullable String str) {
        if (isNull(str)) {
            str = "drive";
        }
        mount = computer.mountWritable(str,makeMount());
        return !isNull(mount);
    }
    public boolean detach(IComputerAccess computer, @Nullable String str) {
        if (isNull(str)) {
            str = "drive";
        }
        try {
            computer.unmount(str);
            return true;
        } catch(Exception ignored) {
            return false;
        }
    }
    @Override
    public void saveAdditional(ValueOutput view) {
        view.putString("uuid", uuid);
        super.saveAdditional(view);
    }

    @Override
    public void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        uuid = view.getStringOr("uuid","");
        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString();
            setChanged();
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        uuid = components.getOrDefault(registry.uuid,"");
        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString();
            setChanged();
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
        super.collectImplicitComponents(componentMapBuilder);
        componentMapBuilder.set(registry.uuid,this.uuid);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput view) {
        view.discard("uuid");
    }
    public IPeripheral peripheral() {
        return periph;
    }
}
