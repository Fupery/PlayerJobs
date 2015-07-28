package me.Fupery.PlayerJobs.Jobs;

import me.Fupery.PlayerJobs.IO.Transaction;
import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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

    public Job(PlayerJobs plugin, OfflinePlayer player) {
        JobInventoryHolder holder = new JobInventoryHolder(plugin, player);
        this.plugin = plugin;
        employer = player.getUniqueId();
        employees = new UUID[9];
        balance = 0;
        inventory = Bukkit.createInventory(holder,
                InventoryType.CHEST, Formatting.inventoryHeading);
        filter = new HashMap<>();
    }

    public static Job deserialize(PlayerJobs plugin, HashMap<String, Object> map) {
        Job job = new Job(plugin, Bukkit.getOfflinePlayer(((UUID) map.get("employer"))));
        job.setBalance(((double) map.get("balance")));
        job.setEmployees((UUID[]) map.get("employees"));
        job.getInventory().setContents(deSerializeInv((map.get("inventory"))));
        job.setFilter((HashMap<Material, Double>) map.get("filter"));

        return job;
    }

    private static List serializeInv(Inventory inventory) {
        ItemStack[] items = inventory.getContents();
        Object[] tags = new Object[items.length];

        for (int i = 0; i < items.length; i++) {

            if (items[i] != null) {
                tags[i] = items[i].serialize();

            } else {
                tags[i] = null;
            }
        }
        return Arrays.asList(tags);
    }

    private static ItemStack[] deSerializeInv(Object list) {
        Object[] tags = ((List) list).toArray();
        ItemStack[] items = new ItemStack[tags.length];

        for (int i = 0; i < items.length; i++) {

            if (tags[i] != null) {

                items[i] = ItemStack.deserialize(((HashMap<String, Object>) tags[i]));

            } else {
                items[i] = null;
            }
        }
        return items;
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
            plugin.getOpenMenus().put(player, new MenuHandler(plugin, this));

        } else if (isEmployee(player)) {

            ItemStack item = player.getItemInHand();
            dump(item, player);
        } else {
            player.sendMessage(Formatting.playerMessage(
                    "Shift & Right-Click to apply for this job"));
        }
    }

    public void onShiftRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (player.getUniqueId().equals(employer)) {
            plugin.getOpenMenus().put(player, new MenuHandler(plugin, this));

        } else if (isEmployee(player)) {
            dump(event.getItem(), player);

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

    private void dump(ItemStack item, Player player) {

        if (item != null && item.getType() != Material.AIR) {

            if (filter != null && filter.containsKey(item.getType())) {

                double wage;
                int remainder = depositItems(item);
                int amountDeposited = item.getAmount() - remainder;

                if (amountDeposited == 0) {
                    player.sendMessage(Formatting.playerMessage(
                            "This job inventory is full! Try again later."));
                    return;
                }

                double round = (filter.get(item.getType()) / 64) * amountDeposited;
                round = Math.round(round * 100);
                wage = round / 100;

                if (balance >= wage) {

                    PlayerJobs.getEconomy().depositPlayer(player, wage);
                    balance -= wage;
                    roundBalance();

                    if (remainder == 0) {

                        player.setItemInHand(null);

                    } else {
                        ItemStack remainingItems = new ItemStack(item);
                        remainingItems.setAmount(remainder);
                        player.setItemInHand(remainingItems);
                    }
                    ItemStack deposit = new ItemStack(item);
                    deposit.setAmount(amountDeposited);
                    inventory.addItem(deposit);

                    new Transaction(plugin, employer, item.getType(), amountDeposited);

                    player.sendMessage(Formatting.playerMessage(String.format(
                            "You were paid $%s for %s %s!",
                            wage, amountDeposited, item.getType().name().toLowerCase())));

                } else {
                    player.sendMessage(Formatting.playerMessage(String.format(
                            "This job doesn't have enough money to buy %s %s for $%s!",
                            item.getAmount(), item.getType().name().toLowerCase(), wage)));
                }
            }
        }
    }

    // Iterates through inventory and checks if there is room for
    // The itemstack in the player's hand - returns the remainder
    private int depositItems(ItemStack item) {

        int amountLeftToDeposit = item.getAmount();

        for (ItemStack i : inventory) {

            if (i == null) {
                amountLeftToDeposit = 0;
                break;

            } else if (i.getType().equals(item.getType())) {
                int amountInSlot = i.getAmount();

                if (amountInSlot < 64) {
                    int freeSpaceInSlot = 64 - amountInSlot;

                    if (freeSpaceInSlot > amountLeftToDeposit) {
                        amountLeftToDeposit = 0;
                        break;

                    } else {
                        amountLeftToDeposit -= freeSpaceInSlot;
                    }
                }
            }
        }
        return amountLeftToDeposit;
    }

    public void roundBalance() {
        double round = balance;
        round = Math.round(round * 100);
        balance = round / 100;
    }

    public void organizeEmployees() {

        UUID[] sort = new UUID[employees.length];
        int k = 0;

        for (UUID id : employees) {
            if (id != null) {
                sort[k] = id;
                k++;
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

        for (int i = 0; i < employees.length; i++) {

            if (employees[i] == null) {
                employees[i] = player.getUniqueId();
                return true;
            }
        }
        return false;
    }

    public HashMap<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("balance", balance);
        map.put("employer", employer);
        map.put("employees", employees);
        map.put("inventory", serializeInv(inventory));
        map.put("filter", filter);
        return map;
    }

    public void onBreak(Location location) {

        OfflinePlayer player = Bukkit.getOfflinePlayer(employer);
        PlayerJobs.getEconomy().depositPlayer(player, balance);
        plugin.deleteJob(location);

        if (plugin.getServer().getOnlinePlayers().contains(((Player) player))) {

            ((Player) player).sendMessage(new String[]{

                    Formatting.playerMessage(String.format(
                            "Your job sign at [%s, %s, %s] has been destroyed",
                            location.getX(), location.getY(), location.getZ())),
                    Formatting.playerMessage(
                            String.format("Added %s to your balance", balance))});
        }
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

class JobInventoryHolder implements InventoryHolder {

    private PlayerJobs plugin;
    private OfflinePlayer player;
    private Inventory inventory;

    JobInventoryHolder(PlayerJobs plugin, OfflinePlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public Inventory getInventory() {

        if (plugin.getServer().getOnlinePlayers().contains(((Player) player))) {
            return ((Player) player).getInventory();
        }
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
