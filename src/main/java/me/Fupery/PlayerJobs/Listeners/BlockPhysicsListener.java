package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsListener implements Listener {

    private PlayerJobs plugin;

    public BlockPhysicsListener (PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPhysicsEvent (BlockPhysicsEvent event) {

        if (event.getBlock().getType() == Material.WALL_SIGN) {
            Sign sign = ((Sign) event.getBlock().getState());

            if (sign.getLine(0).equals(Formatting.signFormat)) {

                if (plugin.getConfig().getBoolean("protect-signs")) {
                    event.setCancelled(true);

                } else {

                    Location location = event.getBlock().getLocation();
                    plugin.getJob(location).onBreak(location);
                }

            }
        }

    }
}
