package me.sen2y.mangoSpawn.managers;

import me.sen2y.mangoSpawn.MangoSpawn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageManager {

    private final File file;
    private FileConfiguration config;

    public MessageManager(File dataFolder) {
        this.file = new File(dataFolder, "messages.yml");

        if (!file.exists()) {
            MangoSpawn.getInstance().saveResource("messages.yml", false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Component get(String key) {
        String message = config.getString(key);
        return MiniMessage.miniMessage().deserializeOrNull(message);
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

}
