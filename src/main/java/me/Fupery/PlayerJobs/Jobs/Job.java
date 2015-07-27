package me.Fupery.PlayerJobs.Jobs;

import me.Fupery.PlayerJobs.IO.Transaction;
import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.JobUI.MenuType;
import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.text.Format;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Job {

    private PlayerJobs plugin;

    private double balance;
    private UUID employer;
    private UUID[] employees;
    private Inventory inventory;
    private HashMap<Material, Double> filter;

    public Job(PlayerJobs plugin, Player player) {
        this.plugin = plugin;
        employer = player.getUniqueId();
        employees = new UUID[9];
        balance = 0;
        inventory = Bukkit.createInventory(player,
                InventoryType.CHEST, Formatting.inventoryHeading);
        filter = new HashMap<>();
    }

    public void onLeftCLick(PlayerInteractEvent event) {
        String[] strings = new String[filter.size()];

        if (filter != null && filter.size() > 0) {

            int i = 0;
            for (Material m : filter.keySet()) {
                strings[i] = Formatting.playerMessage(String.format(
                        "* %s - $%s per stack", m.name(), filter.get(m)));
                i++;
            }
            event.getPlayer().sendMessage(Formatting.playerMessage("This job will pay for:"));
            event.getPlayer().sendMessage(strings);
        }
    }

    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getUniqueId().equals(employer)) {
            plugin.getOpenMenus().put(player, new MenuHandler(plugin, this, MenuType.SETTINGS));

        } else if (isEmployee(player)) {
            ItemStack item = player.getItemInHand();

            if (item != null && item.getType() != Material.AIR) {

                if (filter.containsKey(item.getType())) {
                    int amount = item.getAmount();
                    double wage;
                    HashMap<Integer, ItemStack> leftover = null;

                    if (inventory.firstEmpty() == -1) {

                        leftover = inventory.addItem(item);

                        if (leftover != null) {

                            if (leftover.get(0).getAmount() == amount) {
                                player.sendMessage(Formatting.playerMessage(
                                        "This job inventory is full! Try again later."));
                                return;

                            } else {
                                amount -= leftover.get(0).getAmount();
                            }
                        }
                    }
                    wage = getWage(item.getType(), amount);

                    if (balance >= wage) {

                        PlayerJobs.getEconomy().depositPlayer(player, wage);
                        balance -= wage;
                        inventory.addItem(item);

                        ItemStack inHand = player.getItemInHand();

                        if (inHand.getAmount() == amount) {

                            if (leftover == null) {
                                player.setItemInHand(null);

                            } else {
                                player.setItemInHand(leftover.get(0));
                            }

                        } else {
                            inHand.setAmount(player.getItemInHand().getAmount() - amount);
                            player.setItemInHand(inHand);
                        }
                        new Transaction(plugin, employer, item.getType(), amount);

                        player.sendMessage(Formatting.playerMessage(String.format(
                                "You were paid $%s for %s %s!",
                                wage, amount, item.getType().name().toLowerCase())));

                    } else {
                        player.sendMessage(Formatting.playerMessage(String.format(
                                "This job doesn't have enough money to buy %s %s for $%s!",
                                item.getAmount(), item.getType().name().toLowerCase(), wage)));
                    }
                }
            }
        } else {
            player.sendMessage(Formatting.playerMessage(
                    "Shift & Right-Click to apply for this job"));
        }
    }

    public void onShiftRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (player.getUniqueId().equals(employer)) {
            plugin.getOpenMenus().put(player, new MenuHandler(plugin, this, MenuType.SETTINGS));

        } else if (isEmployee(player)) {
            dump(player);

        } else {
            if (addEmployee(player)) {
                player.sendMessage(Formatting.playerMessage(
                        "You have joined this job! Left click to" +
                                " see what items this sign will pay you to gather."));
            } else {
                player.sendMessage(Formatting.playerMessage(
                        "This job doesn't have any free spaces left! Try again later."));
            }
        }
    }
    private double getWage (Material type, int amount) {
        double wage = (filter.get(type)/64) * amount;
        int round = ((int) (wage * 100));
        return ((double) round) / 100;
    }
    public void organizeEmployees() {
        UUID[] sort = new UUID[employees.length];
        int k = 0;
        for (UUID id : employees) {
            if (id != null) {
                sort[k] = id;
                k ++;
            }
        }
        employees = Arrays.copyOf(sort, k + 1);
    }
    private boolean isEmployee(Player player) {

        if (employees == null) {
            employees = new UUID[9];
        }

        for (UUID id : employees) {

            if (id != null && player.getUniqueId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    private boolean addEmployee(Player player) {
        for (int i = 0; i < employees.length; i ++) {
            if (employees[i] == null) {
                employees[i] = player.getUniqueId();
                return true;
            }
        }
        return false;
    }
    private void dump (Player player) {

    }

    public HashMap<String, Object> serialize () {
        HashMap<String, Object> map = new HashMap<>();
        map.put("balance", balance);
        map.put("employer", employer);
        map.put("employees", employees);
        map.put("inventory", serializeInv(inventory));
        map.put("filter", filter);
        return map;
    }

    public static Job deserialize (PlayerJobs plugin, HashMap<String, Object> map) {
        Job job = new Job(plugin, Bukkit.getPlayer(((UUID) map.get("employer"))));
        job.setBalance(((double) map.get("balance")));
        job.setEmployees((UUID[]) map.get("employees"));
        job.getInventory().setContents(deSerializeInv(( map.get("inventory"))));
        job.setFilter((HashMap<Material, Double>) map.get("filter"));

        return job;
    }

    private static List serializeInv (Inventory inventory) {
        ItemStack[] items = inventory.getContents();
        Object[] tags = new Object[items.length];

        for (int i = 0; i < items.length; i ++) {
            if (items[i] != null) {
                tags[i] = items[i].serialize();
            } else {
                tags[i] = null;
            }
        }
        return Arrays.asList(tags);
    }

    private static ItemStack[] deSerializeInv (Object list) {
        Object[] tags = ((List) list).toArray();
        ItemStack[] items = new ItemStack[tags.length];

        for (int i = 0; i < items.length; i ++) {
            if (tags[i] != null) {
                items[i] = ItemStack.deserialize(((HashMap<String, Object>) tags[i]));
            } else {
                items[i] = null;
            }
        }
        return items;
    }

    public void onBreak (Location location) {
        PlayerJobs.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(
                employer), balance);
        plugin.deleteJob(location);
    }

    public PlayerJobs getPlugin() {
        return plugin;
    }

    public void setPlugin(PlayerJobs plugin) {
        this.plugin = plugin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public UUID getEmployer() {
        return employer;
    }

    public UUID[] getEmployees() {
        return employees;
    }

    public void setEmployees(UUID[] employees) {
        this.employees = employees;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public HashMap<Material, Double> getFilter() {
        return filter;
    }

    public void setFilter(HashMap<Material, Double> filter) {
        this.filter = filter;
    }

}
