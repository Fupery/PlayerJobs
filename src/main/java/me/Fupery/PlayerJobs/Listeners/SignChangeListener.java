package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.IO.NBTTestClass;
import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.Jobs.Job;
import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.io.File;
import java.io.IOException;

public class SignChangeListener implements Listener {

    private PlayerJobs plugin;

    public SignChangeListener(PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {

        if (event.getLine(0).equals("[Job]")) {
            Player player = event.getPlayer();
            if (!player.hasPermission("playerjobs.employer")) {
                player.sendMessage(Formatting.playerMessage(
                        "You don't have permission to create a Job!"));
                event.setLine(0, "[Nope]");
                return;
            }
            event.setLine(0, Formatting.signFormat);
            Job job = new Job(plugin, player);
            plugin.getJobList().put(event.getBlock().getLocation(), job);
            plugin.getOpenMenus().put(player,
                    new MenuHandler(plugin, job));
        } else if (event.getLine(0).equals("test")) {
            try {
                NBTTestClass.saveNBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
