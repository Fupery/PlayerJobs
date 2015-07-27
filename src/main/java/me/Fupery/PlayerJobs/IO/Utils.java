package me.Fupery.PlayerJobs.IO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public class Utils {

    public static String getTag (Location location) {
        return String.format("%s_%s_%s.dat", ((int) location.getX()),
                ((int) location.getY()), ((int) location.getZ()));
    }

    public static Location getLocation (String tag, World world) {

        int[] coordinates = new int[3];
        char[] chars = tag.toCharArray();
        int k = 0;
        String num = "";

        for (int i = 0; i < 3; i ++) {
            Bukkit.getLogger().info(num);
            if (Character.isDigit(chars[i]) || chars[i] == '-') {
                num += chars[i];
            } else {
                coordinates[i] = Integer.parseInt(num);
                k ++;
                num = "";
            }
        }
        Bukkit.getLogger().info(coordinates[0] + coordinates[1] + coordinates[2] + "");
        return new Location(world, coordinates[0], coordinates[1],
                coordinates[1]);
    }

    public static String formatJSON (Player player, String[] strings) {
        String string = String.format(cmd + head + tail, player);
        return string;
    }
    private static final String
            cmd = "/tellraw %s ",
            leader = "[\"\",",
            head = "{\"text\":\"[&s]\",\"color\":\"yellow\"," +
                    "\"bold\":\"true\",\"hoverEvent\":{action:show_item," +
                    "value:\"{id:minecraft:stone,tag:{display:{Name:Test,Lore:[",
            tail = "]}}}\"}}",
            trail = "]";

    public static int[] getChest (SignChangeEvent event) {

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
