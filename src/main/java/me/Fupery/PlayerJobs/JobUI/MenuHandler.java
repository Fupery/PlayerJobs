package me.Fupery.PlayerJobs.JobUI;

import me.Fupery.PlayerJobs.JobUI.SubMenus.JobMenu;
import me.Fupery.PlayerJobs.JobUI.SubMenus.MenuDump;
import me.Fupery.PlayerJobs.Jobs.Job;
import me.Fupery.PlayerJobs.PlayerJobs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MenuHandler {

    private Job job;
    private PlayerJobs plugin;
    private Player player;
    private AbstractMenu root;
    private AbstractMenu branch;
    private MenuType menuType;

    private boolean branching;

    public MenuHandler(PlayerJobs plugin, Job job, MenuType menuType) {
        this.job = job;
        this.plugin = plugin;
        this.menuType = menuType;
        player = Bukkit.getPlayer(job.getEmployer());
        branching = false;
        openRoot();
    }

    public void openRoot() {

        if (menuType == MenuType.SETTINGS) {
            root = new JobMenu(this);

        } else if (menuType == MenuType.DUMP) {
            root = new MenuDump(this);
        }
            root.open();
    }

    public void openBranch(AbstractMenu menu) {
        branching = true;
        branch = menu;
        branch.open();
    }

    public void closeRoot() {
        root.close();
        root = null;
        if (!branching) {
            plugin.getOpenMenus().remove(player);
        }
    }

    public void closeBranch() {
        branching = false;
        branch.close();
        branch = null;
    }
    public void openInv(Inventory inventory) {
        branching = true;
        player.openInventory(inventory);
    }

    public void closeInv() {
        branching = false;
    }

    public AbstractMenu getMenu(Inventory inventory) {

        if (root != null && root.getInventory().equals(inventory)) {
            return root;

        } else if (branch != null && branch.getInventory().equals(inventory)) {
            return branch;

        } else {
            return null;
        }
    }

    public boolean isBranching() {
        return branching;
    }

    public void setBranch(AbstractMenu menu) {
        if (menu != root) {
            branch = menu;
        }
    }

    public AbstractMenu getBranch() {
        return branch;
    }

    public Job getJob() {
        return job;
    }

    public PlayerJobs getPlugin() {
        return plugin;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractMenu getRoot() {
        return root;
    }
}
