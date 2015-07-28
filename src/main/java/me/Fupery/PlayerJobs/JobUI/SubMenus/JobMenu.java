package me.Fupery.PlayerJobs.JobUI.SubMenus;

import me.Fupery.PlayerJobs.IO.JSONBuilder;
import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import me.Fupery.PlayerJobs.JobUI.Buttons.*;
import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class JobMenu extends AbstractMenu {

    private double playerBalance;
    private double initialBal;

    public JobMenu(MenuHandler handler) {
        super(handler);
        menu = Bukkit.createInventory(handler.getPlayer(),
                InventoryType.HOPPER, Formatting.menuHeading);
        Economy economy = PlayerJobs.getEconomy();

        if (economy != null) {
            playerBalance = economy.getBalance(handler.getPlayer());
            initialBal = handler.getJob().getBalance();
        }
        setupRootMenu();
    }

    private void setupRootMenu() {
        buttons = new AbstractButton[menu.getSize()];

        buttons[0] = new ButtonLink(this, new String[]{
                ChatColor.GREEN + "Filter Items",
                "Click to select the items that",
                "your employees will deliver to you.",
                ChatColor.YELLOW + "Drop items in this menu to",
                ChatColor.YELLOW + "add them to your list."},
                Material.GRASS, new MenuFilter(handler));

        buttons[1] = new ButtonCounter(this, new String[]{
                ChatColor.GREEN + "Job Balance",
                ChatColor.GOLD + "- $" + handler.getJob().getBalance(),
                "Money will be payed to your",
                "employees from your job balance.",
                ChatColor.YELLOW + "Left-click to deposit money,",
                ChatColor.YELLOW + "Right-click to retrieve money"},
                Material.EMERALD, handler.getJob().getBalance(), 10,
                playerBalance, false, true);

        buttons[2] = new ButtonLink(this, new String[]{
                ChatColor.GREEN + "Employees",
                "Click to view all your current,",
                "employees and review their work",
                ChatColor.YELLOW + "Right-click to fire an employee"},
                Material.SKULL_ITEM, new MenuEmployees(handler));

        buttons[3] = new ButtonChat(this, new String[]{
                ChatColor.GREEN + "Job Logs",
                "Click to review the activity at",
                "this job sign for the past week."},
                Material.BOOK_AND_QUILL);

        buttons[4] = new ButtonInv(this, new String[]{
                ChatColor.GREEN + "Inventory",
                "Click to view the items that your",
                "employees have collected for you."},
                Material.CHEST, handler.getJob().getInventory());

        UUID[] employees = handler.getJob().getEmployees();

        if (employees == null) {
            buttons[2].setAmount(0);

        } else {
            int i;

            for (i = 0; i < employees.length; i++) {

                if (employees[i] == null) {
                    break;
                }
            }
            buttons[2].setAmount(i);
        }
        buttons[2].setDurability((short) 3);

        menu.setContents(buttons);
    }

    @Override
    public void passValues(AbstractButton button, Object value) {

        if (button instanceof ButtonCounter) {
            ItemMeta meta = button.getItemMeta();
            List<String> lore = meta.getLore();
            lore.set(0, ChatColor.GOLD + "- $" + value);
            meta.setLore(lore);
            button.setItemMeta(meta);

        } else if (button instanceof ButtonChat) {
            new JSONBuilder(handler.getPlugin(), handler.getPlayer());
        }
    }

    @Override
    public void close() {
        handler.getJob().setBalance(((double) buttons[1].getReturnValue()));
        handler.getJob().roundBalance();
        double d = handler.getJob().getBalance() - initialBal;
        EconomyResponse r;
        String s;
        if (d < 0) {
            r = PlayerJobs.getEconomy().depositPlayer(handler.getPlayer(), -d);
            s = "withdrew %s%s from";
        } else {
            r = PlayerJobs.getEconomy().withdrawPlayer(handler.getPlayer(), d);
            s = "deposited %s%s into";
        }

        if (d != 0 && !handler.isBranching()) {
            double t = (d > 0) ? d : -d;
            handler.getPlayer().sendMessage(Formatting.playerMessage(
                    String.format("Successfully " + s + " your job",
                            ChatColor.GOLD + "$" + t, ChatColor.GRAY)));
        }
    }
}
