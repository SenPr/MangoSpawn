package me.sen2y.mangoSpawn;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.sen2y.mangoSpawn.commands.MangoSpawnCommands;
import me.sen2y.mangoSpawn.commands.SetSpawnCommand;
import me.sen2y.mangoSpawn.commands.SpawnCommand;
import me.sen2y.mangoSpawn.managers.LastLocationManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public final class MangoSpawn extends JavaPlugin {

    private static MangoSpawn instance;
    private Location spawn;
    private LastLocationManager lastLocationManager;

    @Override
    public void onEnable() {
        instance = this;
        loadSpawn();
        lastLocationManager = new LastLocationManager();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(MangoSpawnCommands.createCommand(), "main commands");
            commands.registrar().register(SpawnCommand.createCommand(), "Teleport to spawn");
            commands.registrar().register(SetSpawnCommand.createCommand(), "Set spawn");
        });

    }

    @Override
    public void onDisable() {
        lastLocationManager.teleportAllBack();
    }

    public static MangoSpawn getInstance() {
        return instance;
    }

    private File getSpawnFile() {
        return new File(this.getDataFolder(), "spawn.yml");
    }
    private void loadSpawn() {
        File file = getSpawnFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String worldName = config.getString("world");
        if (worldName == null) {
            return;
        }

        this.spawn = new Location(
                Bukkit.getWorld(worldName),
                config.getDouble("x"),
                config.getDouble("y"),
                config.getDouble("z"),
                config.getFloatList("yaw").getFirst(),
                config.getFloatList("pitch").getFirst()
        );
    }
    public void saveSpawn(Location loc) {
        File file = getSpawnFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("world", loc.getWorld().getName());
        config.set("x", loc.getX());
        config.set("y", loc.getY());
        config.set("z", loc.getZ());
        config.set("yaw", loc.getYaw());
        config.set("pitch", loc.getPitch());

        try {
            config.save(file);
            this.spawn = loc;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public @Nullable Location getSpawn() {
        return getInstance().spawn;
    }
    public LastLocationManager getLastLocationManager() {
        return this.lastLocationManager;
    }

}
