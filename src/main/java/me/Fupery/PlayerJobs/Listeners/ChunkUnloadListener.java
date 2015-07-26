package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.PlayerJobs;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkUnloadListener extends BukkitRunnable implements Listener {

    private PlayerJobs plugin;
    private Chunk chunk;

    public ChunkUnloadListener (PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkUnload (ChunkUnloadEvent event) {
        this.chunk = event.getChunk();
        runTaskAsynchronously(plugin);
    }

    @Override
    public void run() {

        for (final Location location : plugin.getJobList().keySet()) {

            if (location.getChunk().equals(chunk)) {
                plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.saveJob(location);
                    }
                });
            }
        }
    }
}

