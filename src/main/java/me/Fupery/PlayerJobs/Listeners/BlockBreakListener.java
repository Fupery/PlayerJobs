package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.Jobs.Job;
import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private PlayerJobs plugin;

    public BlockBreakListener(PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Block block = event.getBlock();

        if (block.getType().equals(Material.SIGN_POST)
                || block.getType().equals(Material.WALL_SIGN)) {
            Sign sign = ((Sign) block.getState());

            if (sign.getLine(0).equals(Formatting.signFormat)) {
                Job job = plugin.getJob(event.getBlock().getLocation());

                if (job == null) {
                    return;
                }

                if (plugin.getConfig().getBoolean("protect-signs")) {

                    Player player = event.getPlayer();

                    if (!Bukkit.getPlayer(job.getEmployer()).equals(player)
                            && !player.hasPermission("playerjobs.admin")) {

                        player.sendMessage(Formatting.playerMessage(
                                "You don't have permission to break this job sign"));
                        event.setCancelled(true);
                        return;
                    }
                }
                job.onBreak(event.getBlock().getLocation());
            }
        }
    }
}
