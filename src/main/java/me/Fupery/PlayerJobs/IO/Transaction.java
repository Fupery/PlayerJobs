package me.Fupery.PlayerJobs.IO;

import me.Fupery.PlayerJobs.PlayerJobs;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Transaction extends BukkitRunnable {

    private PlayerJobs plugin;
    private UUID employer;
    private UUID employee;
    private double amount;
    private Material item;

    public Transaction(PlayerJobs plugin, UUID employer, UUID employee,
                       double amount, Material item) {
        this.employer = employer;
        this.employee = employee;
        this.amount = amount;
        this.item = item;
        runTaskAsynchronously(plugin);
    }

    @Override
    public void run() {
        //TODO - save to log files
        ;
    }
}
