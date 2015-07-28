package me.Fupery.PlayerJobs.IO;

import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static me.Fupery.PlayerJobs.IO.Utils.dateFormat;

public class JSONBuilder extends BukkitRunnable {

    private static final String
            command = "tellraw %s ", head = "[\"\",",
            body = "{text:\"[%s]\",\"color\":\"%s\"," +
                    "\"bold\":\"true\",\"hoverEvent\":{action:show_item," +
                    "value:\"{id:minecraft:stone,tag:{display:{Name:%s,Lore:[",
            tail = "]}}}\"}}",
            trail = "]";

    PlayerJobs plugin;
    Player player;
    private File file;

    public JSONBuilder(PlayerJobs plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        file = new File(plugin.getLogs(), player.getUniqueId().toString());
        runTaskAsynchronously(plugin);
    }

    @Override
    public void run() {

        String message = String.format(command, player.getName());
        String colour = "gold";

        HashMap<Date, ConfigurationSection> map;
        try {
            map = getLogs();

        } catch (ParseException | IOException e) {
            return;
        }
        Set<Date> list = map.keySet();
        Date[] dates = new Date[list.size()];
        dates = list.toArray(dates);
        List<Date> sort = Arrays.asList(dates);
        Collections.sort(sort);
        dates = sort.toArray(dates);

        if (dates.length > 1) {
            message += head;

            for (int i = 0; i < dates.length; i++) {
                colour = (i % 2 == 0) ? "gray" : "dark_gray";
                message += getSubMessage(dates[i], map.get(dates[i]), colour);

                if (i < dates.length - 1) {
                    message += ",";
                }
            }
            message += trail;

        } else {
            message += getSubMessage(dates[0], map.get(dates[0]), colour);
        }
        player.sendMessage(Formatting.playerMessage("Logs for the past week - hover over the dates to view"));
        Server server = plugin.getServer();
        server.dispatchCommand(server.getConsoleSender(), message);
    }

    public HashMap<Date, ConfigurationSection> getLogs()
            throws ParseException, IOException {

        HashMap<Date, ConfigurationSection> map = new HashMap<>();

        if (!file.exists()) {
            file.createNewFile();
        }
        FileConfiguration log = YamlConfiguration.loadConfiguration(file);

        for (String s : log.getKeys(false)) {

            if (log.get(s) != null) {
                map.put(dateFormat.parse(s),
                        ((ConfigurationSection) log.get(s)));
            }
        }

        return map;
    }

    public String getSubMessage(Date date, ConfigurationSection section, String colour) {

        String logs = "";
        String name = dateFormat.format(date).substring(0, 5);

        Set<String> set = section.getKeys(false);
        String[] keys = new String[set.size()];
        keys = set.toArray(keys);

        for (int i = 0; i < keys.length; i++) {
            logs += String.format("\\\"%s - %s\\\"", keys[i], section.get(keys[i]));

            if (i < keys.length - 1) {
                logs += ",";
            }
        }
        return String.format(body + logs + tail, name, colour, "Logs -");
    }
}
