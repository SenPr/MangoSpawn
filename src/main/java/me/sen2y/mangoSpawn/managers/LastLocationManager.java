package me.sen2y.mangoSpawn.managers;

import me.sen2y.mangoSpawn.utils.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class LastLocationManager {

    private final HashMap<UUID, Location> lastLocations;

    public LastLocationManager() {
        lastLocations = new HashMap<>();
    }

    public void saveLocation(UUID uuid, Location loc) {
        lastLocations.put(uuid, loc);
    }

    public void removePlayer(UUID uuid) {
        lastLocations.remove(uuid);
    }

    public Location getLocation(UUID uuid) {
        return lastLocations.get(uuid);
    }

    public boolean teleportBack(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        Location loc = getLocation(uuid);
        if (loc == null) return false; // TODO: default location in main world
        if (player.hasPermission(Permissions.BYPASS)) return false;
        player.teleport(loc);
        removePlayer(uuid);
        return true;
    }

    public void teleportAllBack() {
        lastLocations.forEach((uuid, location) -> teleportBack(uuid));
    }

}
