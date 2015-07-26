package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.JobUI.MenuType;
import me.Fupery.PlayerJobs.Jobs.Job;
import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    private PlayerJobs plugin;

    public SignChangeListener(PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        if (event.getLine(0).equals("[Job]")) {
            event.setLine(0, Formatting.signFormat);
            Player player = event.getPlayer();
            if (!player.hasPermission("playerjobs.employer")) {
                player.sendMessage(Formatting.playerMessage(
                        "You don't have permission to create a Job!"));
                return;
            }
            int[] chest = getChest(event);
            if (chest != null) {
                Job job = new Job(plugin, player);
                plugin.getJobList().put(event.getBlock().getLocation(), job);
                plugin.getOpenMenus().put(player,
                        new MenuHandler(plugin, job, MenuType.SETTINGS));
            }
        }
    }
    public int[] getChest (SignChangeEvent event) {

        for (int x = -1; x < 2; x++) {

            for (int z = -1; z < 2; z ++) {
                Location l = event.getBlock().getLocation().add(x, 0, z);

                if (l.getBlock() != null &&
                l.getBlock().getType().equals(Material.CHEST)) {

                    int[] disp = new int[2];
                    disp[0] = x; disp[1] = z;
                    return disp;
                }
            }
        }
        return null;
    }
}
