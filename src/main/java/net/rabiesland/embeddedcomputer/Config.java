package net.rabiesland.embeddedcomputer;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class Config {
    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(ResourceLocation.fromNamespaceAndPath("embeddedcomputer", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("config.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting) // not needed, pretty print by default
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public int ZIP_DISK_STORAGE = 5000000;

    @SerialEntry
    public int FLASH_CARD_STORAGE = 2500000;

    @SerialEntry
    public int DEBUG_ITEM_STORAGE = 2147483647;

    @SerialEntry
    public int HARD_DRIVE_STORAGE = 25000000;
}