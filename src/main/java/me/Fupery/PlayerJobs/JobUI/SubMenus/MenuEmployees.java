package me.Fupery.PlayerJobs.JobUI.SubMenus;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import me.Fupery.PlayerJobs.JobUI.Buttons.AbstractButton;
import me.Fupery.PlayerJobs.JobUI.Buttons.ButtonToken;
import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class MenuEmployees extends AbstractMenu {

    public MenuEmployees(MenuHandler handler) {

        super(handler);
        menu = Bukkit.createInventory(handler.getPlayer(),
                9, Formatting.employeeMenuHeading);

        if (handler.getJob().getEmployees() == null) {
            handler.getJob().setEmployees(new UUID[9]);
        }
        UUID[] employees = handler.getJob().getEmployees();
        int e;

        for (e = 0; e < employees.length; e++) {
            if (employees[e] == null) {
                break;
            }
        }


        buttons = new AbstractButton[e];

        for (int i = 0; i < buttons.length; i++) {

            if (employees[i] != null) {

                OfflinePlayer p = Bukkit.getOfflinePlayer(employees[i]);

                buttons[i] = new ButtonToken(this, new String[]{
                        ChatColor.GREEN + p.getName(), "Is ok I guess"
                }, Material.SKULL_ITEM, true);
                buttons[i].setDurability((short) 3);

                SkullMeta meta = ((SkullMeta) buttons[i].getItemMeta());

                meta.setOwner(p.getName());
                meta.setDisplayName(p.getName());

                buttons[i].setItemMeta(meta);
            }
        }
        menu.setContents(buttons);
    }

    @Override
    public void close() {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] != null
                    && ((boolean) buttons[i].getReturnValue())) {
                handler.getJob().getEmployees()[i] = null;
            }
        }
        handler.getJob().organizeEmployees();
    }

    @Override
    public void passValues(AbstractButton button, Object value) {
        int i;
        for (i = 0; i < buttons.length; i++) {
            if (button.equals(buttons[i])) {
                break;
            }
        }
        ItemMeta meta = button.getItemMeta();
        OfflinePlayer player = Bukkit.getOfflinePlayer(
                handler.getJob().getEmployees()[i]);
        if ((boolean) value) {
            String title = ChatColor.STRIKETHROUGH +
                    player.getName();
            meta.setDisplayName(ChatColor.RED +
                    title + ChatColor.YELLOW + " (Fired)");
        } else {
            meta.setDisplayName(ChatColor.GREEN + player.getName());
        }
        button.setItemMeta(meta);
    }
}
