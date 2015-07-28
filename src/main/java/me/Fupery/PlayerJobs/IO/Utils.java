package me.Fupery.PlayerJobs.IO;

import org.bukkit.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Utils {

    public static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static String getTag(Location location) {
        return String.format("%s_%s_%s.dat", ((int) location.getX()),
                ((int) location.getY()), ((int) location.getZ()));
    }
}
