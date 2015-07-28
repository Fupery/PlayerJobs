package me.Fupery.PlayerJobs.IO;

import me.Fupery.PlayerJobs.PlayerJobs;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static me.Fupery.PlayerJobs.IO.Utils.dateFormat;

public class Transaction extends BukkitRunnable {

    private File file;
    private Material item;
    private int amount;
    private String date;


    public Transaction(PlayerJobs plugin, UUID employer, Material item, int amount) {
        this.amount = amount;
        this.item = item;
        Date d = new Date();
        date = dateFormat.format(d);

        file = new File(plugin.getLogs(), employer.toString());
        runTaskAsynchronously(plugin);
    }

    @Override
    public void run() {

        if (!file.exists()) {

            try {
                file.createNewFile();

            } catch (IOException e) {
                return;
            }
        }
        FileConfiguration log = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection section;

        if (!log.contains(date)) {
            section = log.createSection(date);

        } else {
            section = log.getConfigurationSection(date);
        }

        if (!section.contains(item.name()) || section.get(item.name()) == null) {
            section.set(item.name(), amount);

        } else {
            int i = ((int) section.get(item.name())) + amount;
            section.set(item.name(), i);
        }
        try {
            updateConfig(log);
            log.save(file);

        } catch (IOException | ParseException e) {
        }
    }

    private void updateConfig(FileConfiguration log) throws ParseException {
        Set<String> keys = log.getKeys(false);
        ArrayList<Date> dates = new ArrayList<>();
        if (keys != null) {
            if (keys.size() > 7) {
                for (String s : keys) {
                    dates.add(dateFormat.parse(s));
                }
                Collections.sort(dates);
                log.set(dateFormat.format(dates.get(0)), null);
            }
        }
    }
}