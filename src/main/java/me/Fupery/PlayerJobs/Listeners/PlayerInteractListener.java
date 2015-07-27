package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.Jobs.Job;
import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private PlayerJobs plugin;

    public PlayerInteractListener(PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block != null
                && (block.getType().equals(Material.SIGN_POST)
                || block.getType().equals(Material.WALL_SIGN))) {

            Sign sign = ((Sign) block.getState());

            if (sign.getLine(0).equals(Formatting.signFormat)) {
                Job job = plugin.getJob(block.getLocation());

                if (job == null) {
                    return;
                }
                switch (event.getAction()) {

                    case LEFT_CLICK_BLOCK:
                        job.onLeftCLick(event);
                        break;

                    case RIGHT_CLICK_BLOCK:

                        if (event.getPlayer().isSneaking()) {
                            job.onShiftRightClick(event);

                        } else {
                            job.onRightClick(event);
                        }
                        break;
                    default:
                }
            }
        }
    }
}
