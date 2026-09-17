package net.rabiesland.embeddedcomputer.secure;

import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import net.rabiesland.embeddedcomputer.embedded.IEmbeddedComputer;

public class SecureComputerAPI  implements ILuaAPI {
    private final IEmbeddedComputer brain;
    public SecureComputerAPI(IEmbeddedComputer brain) {
        this.brain = brain;
    }
    @Override
    public String[] getNames() {
        return new String[]{"security"};
    }

    @LuaFunction
    public final String hashStrSHA256(String str) {
        return HashUtil.hashStrSHA256(str);
    }
    @LuaFunction
    public final String hashStrSHA512(String str) {
        return HashUtil.hashStrSHA512(str);
    }
    @LuaFunction
    public final String hashStrMurmur3(String str) {
        return HashUtil.hashStrMurmur3(str);
    }
    @LuaFunction
    public final String hashStrAdler32(String str) {
        return HashUtil.hashStrAdler32(str);
    }
    @LuaFunction
    public final String hashStrSipHash24(String str) {
        return HashUtil.hashStrSipHash24(str);
    }
}
