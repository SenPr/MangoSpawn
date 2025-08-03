package me.sen2y.mangoSpawn.events;

import me.sen2y.mangoSpawn.MangoSpawn;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportEvents implements Listener {

    @EventHandler
    public void TeleportEvent(PlayerTeleportEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        String worldFrom = from.getWorld().getName();
        String worldTo = to.getWorld().getName();

        if (worldFrom.equals("world") && worldTo.equals("spawn")) {
            MangoSpawn.getInstance().getLastLocationManager()
                    .saveLocation(event.getPlayer().getUniqueId(), from);
        }
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent event) {
        MangoSpawn.getInstance().getLastLocationManager().teleportBack(event.getPlayer().getUniqueId());
    }

}
