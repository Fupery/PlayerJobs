package me.Fupery.PlayerJobs;

import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.Jobs.Job;
import me.Fupery.PlayerJobs.Listeners.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

import static me.Fupery.PlayerJobs.IO.Utils.getTag;

public class PlayerJobs extends JavaPlugin {

    private HashMap<Location, Job> jobList;
    private HashMap<Player, MenuHandler> openMenus;
    private static Economy economy = null;

    private File data;
    private File logs;

    @Override
    public void onEnable() {

        jobList = new HashMap<Location, Job>();
        openMenus = new HashMap<Player, MenuHandler>();


        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new BlockBreakListener(this), this);
        manager.registerEvents(new BlockPhysicsListener(this), this);
        manager.registerEvents(new InventoryClickListener(this), this);
        manager.registerEvents(new InventoryDragListener(this), this);
        manager.registerEvents(new InventoryCloseListener(this), this);
        manager.registerEvents(new PlayerInteractListener(this), this);
        manager.registerEvents(new SignChangeListener(this), this);

        if (!setupEconomy()) {
            getLogger().warning("Economy not found, disabling plugin.");
        }

        setupRegistry();
    }

    @Override
    public void onDisable() {

        getLogger().info("Archiving jobs ...");

        if (openMenus.size() > 0) {
            for (Player player : openMenus.keySet()) {
                openMenus.get(player).closeBranch();
                openMenus.get(player).closeRoot();
            }
        }
        Set<Location> keys = jobList.keySet();
         if (jobList.size() > 0) {
            for (Location location : keys) {
                saveJob(location);
            }
        }
    }

    private boolean setupEconomy() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer()
                .getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupRegistry() {
        saveDefaultConfig();

        File dir = getDataFolder();
        data = new File(dir, "data");
        logs = new File(dir, "logs");

        if (!data.exists()) {
            if (!data.mkdir()) {
                Bukkit.getLogger().warning("Could not create data folder");
                return false;
            }
        }
        if (!logs.exists()) {
            if (!logs.mkdir()) {
                Bukkit.getLogger().warning("Could not create logs folder");
                return false;
            }
        }
        return true;
    }

    public boolean jobExists (Location location) {

        if (jobList.containsKey(location)
                || new File(data, getTag(location)).exists()) {
            return true;
        }
        return false;
    }

    public Job getJob (Location location) {

        if (jobList != null && jobList.containsKey(location)) {
            return jobList.get(location);

        } else {
            File file = new File(data, getTag(location));

            if (file.exists()) {
                Object o;

                try {
                    ObjectInputStream in = new ObjectInputStream(
                            new FileInputStream(file));
                    o = in.readObject();
                    in.close();

                } catch (IOException | ClassNotFoundException e) {
                    o = null;
                }

                if (o != null) {
                    Job job = Job.deserialize(this, (HashMap<String, Object>) o);
                    jobList.put(location, job);
                    return job;
                }
            }
        }
        return null;
    }

    public void saveJob (Location location) {

        if (jobList.containsKey(location)) {
            File file = new File(data, getTag(location));

            try {

                if (!file.exists()) {
                    file.createNewFile();
                }
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(jobList.get(location).serialize());
                out.flush();
                out.close();

            } catch (IOException e) {
                getLogger().info("IOException when saving " + getTag(location));
                return;
            }
        }
        jobList.remove(location);
    }

    public void deleteJob (Location location) {
        File file = new File (data, getTag(location));

        if (jobList.containsKey(location)) {
            jobList.remove(location);
            return;
        }

        if (file.exists()) {
            if ( !file.delete()) {
                getLogger().info("file " + file.getName() + "could not be deleted.");
            }
        }
    }


    public HashMap<Location, Job> getJobList() {
        return jobList;
    }

    public HashMap<Player, MenuHandler> getOpenMenus() {
        return openMenus;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public File getData() {
        return data;
    }

    public File getLogs() {
        return logs;
    }
}
