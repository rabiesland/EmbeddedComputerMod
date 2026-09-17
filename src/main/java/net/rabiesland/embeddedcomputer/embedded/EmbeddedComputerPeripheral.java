/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.rabiesland.embeddedcomputer.embedded;

import dan200.computercraft.api.filesystem.WritableMount;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ServerComputer;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.rabiesland.embeddedcomputer.embedded.block.EmbeddedComputerBlockEntity;
import net.rabiesland.embeddedcomputer.secure.HashUtil;

import java.nio.channels.SeekableByteChannel;
import java.util.Scanner;

import static java.util.Objects.isNull;
import static net.rabiesland.embeddedcomputer.main.log;

public class EmbeddedComputerPeripheral implements IPeripheral {
    public final EmbeddedComputerBlockEntity comp;
    public EmbeddedComputerPeripheral(AbstractComputerBlockEntity owner) {
        super();
        this.comp = (EmbeddedComputerBlockEntity) owner;
    }


    @Override
    public String getType() {
        return "embeddedcomputer";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        boolean same = false;
        if (this == other) same = true;
        if (other instanceof EmbeddedComputerPeripheral comp1 && comp1.comp == comp) same = true;
        return same;
    }

    public ServerEmbeddedComputer getServerComp() {
        var comp1 = comp.getServerComputer();
        return (ServerEmbeddedComputer) comp1;
    }

    @LuaFunction
    public final boolean isOn() {
        var comp1= getServerComp();
        return !isNull(comp1) && comp1.isOn();
    }

    @LuaFunction
    public final int getId() {
        var comp1= getServerComp();
        if (!isNull(comp1)) return comp1.getID();
        else return -1;
    }

    @LuaFunction
    public final void reboot() {
        var comp1 = getServerComp();
        if (!isNull(comp1)) comp1.reboot();
    }

    @LuaFunction(mainThread = true)
    public final boolean format() {
        var comp1 = getServerComp();
        WritableMount mnt;
        if (!isNull(comp1)) {
            try {
                mnt = comp1.createRootMount();
                if (mnt.exists(".LOCKED") || mnt.exists(".LOCKED_HASHED")) {
                    return false;
                }
                try {
                    mnt.delete("/");
                    comp1.reboot();
                    return true;
                }catch(Exception ignored){
                    comp1.reboot();
                    return false;
                }
            }catch(Exception ex){
                log.info(ex.getMessage(),ex.fillInStackTrace());
                return false;
            }
        }
        return false;
    }

    @LuaFunction(mainThread = true)
    public final void unlockPlainText(String pass1) {
        ServerComputer comp1 = getServerComp();
        if (!isNull(comp)) {
            WritableMount mnt;
            SeekableByteChannel root = null;
            Scanner scan = null;
            try {
                mnt = comp1.createRootMount();
                if (mnt.exists(".LOCKED")) {
                    String pass = "";
                    root = comp1.createRootMount().openForRead(".LOCKED");
                    scan = new Scanner(root);
                    while (scan.hasNext()) {
                        pass+=scan.next();
                    }
                    scan.close();
                    if (!pass.equals(pass1)) {
                        root.close();
                        return;
                    }
                    mnt.delete(".LOCKED");
                    return;
                }
            } catch (Exception ex) {
                log.warn(ex.toString());
                if (!isNull(root)) {
                    try {
                        root.close();
                    } catch (Exception ignored) {}
                }
                if (!isNull(scan)) {
                    scan.close();
                }
                return;
            }
        }
    }

    @LuaFunction(mainThread = true)
    public final void unlockHashed(String pass1,String hashType) {
        ServerComputer comp1 = getServerComp();
        String hashedPass;
        if (hashType.equalsIgnoreCase("sha256")) {
            hashedPass = HashUtil.hashStrSHA256(pass1);
            for (int i=0; i <= pass1.length(); i++) {
                hashedPass = HashUtil.hashStrSHA256(hashedPass);
            }
        } else if (hashType.equalsIgnoreCase("murmur3")) {
            hashedPass = HashUtil.hashStrMurmur3(pass1);
            for (int i=0; i <= pass1.length(); i++) {
                hashedPass = HashUtil.hashStrMurmur3(hashedPass);
            }
        } else if (hashType.equalsIgnoreCase("adler32")) {
            hashedPass = HashUtil.hashStrAdler32(pass1);
            for (int i=0; i <= pass1.length(); i++) {
                hashedPass = HashUtil.hashStrAdler32(hashedPass);
            }
        } else if (hashType.equalsIgnoreCase("siphash24")) {
            hashedPass = HashUtil.hashStrSipHash24(pass1);
            for (int i=0; i <= pass1.length(); i++) {
                hashedPass = HashUtil.hashStrSipHash24(hashedPass);
            }
        } else {
            return;
        }
        for (int i=0; i <= pass1.length(); i++) {
            hashedPass = HashUtil.hashStrSHA512(hashedPass);
        }
        if (!isNull(comp)) {
            WritableMount mnt;
            SeekableByteChannel root = null;
            Scanner scan = null;
            try {
                mnt = comp1.createRootMount();
                if (mnt.exists(".LOCKED_HASHED")) {
                    String pass = "";
                    root = comp1.createRootMount().openForRead(".LOCKED_HASHED");
                    scan = new Scanner(root);
                    while (scan.hasNext()) {
                        pass+=scan.next();
                    }
                    scan.close();
                    if (!pass.equals(hashedPass)) {
                        root.close();
                        return;
                    }
                    mnt.delete(".LOCKED_HASHED");
                    return;
                }
            } catch (Exception ex) {
                log.warn(ex.toString());
                if (!isNull(root)) {
                    try {
                        root.close();
                    } catch (Exception ignored) {}
                }
                if (!isNull(scan)) {
                    scan.close();
                }
                return;
            }
        }
    }

    @LuaFunction(mainThread = true)
    public final void unlock(String pass1) {
        unlockHashed(pass1,"sha256");
    }

    // Generic functions
    @Override
    public void attach(IComputerAccess computer) {}
    @Override
    public void detach(IComputerAccess computer) {}

    @Override
    public Object getTarget() {
        return comp;
    }

    public static IPeripheral getPeripheral(BlockEntity blockEntity, Direction direction) {
        return ((EmbeddedComputerBlockEntity)blockEntity).peripheral();
    }
}
