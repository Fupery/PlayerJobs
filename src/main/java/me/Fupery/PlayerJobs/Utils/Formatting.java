package me.Fupery.PlayerJobs.Utils;

import org.bukkit.ChatColor;

public class Formatting {

    public static final String
            menuHeading = "§1[Job] Settings Menu",
            filterMenuHeading = "§3[Job] Filter Place items below",
            employeeMenuHeading = "§9[Job] Employees Q/drop to fire",
            logMenuHeading = "Logs for the past seven days",
            inventoryHeading = "§5[Job] inventory",
            signFormat = String.format("%s[%sJob%s]", ChatColor.DARK_BLUE,
                    ChatColor.BLUE, ChatColor.DARK_BLUE);


    public static String playerMessage(String string) {
        return ChatColor.LIGHT_PURPLE + "[Jobs] " + ChatColor.GRAY + string;
    }
}
