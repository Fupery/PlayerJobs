package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import me.Fupery.PlayerJobs.JobUI.SubMenus.JobMenu;
import me.Fupery.PlayerJobs.JobUI.SubMenus.MenuEmployees;
import me.Fupery.PlayerJobs.JobUI.SubMenus.MenuFilter;
import me.Fupery.PlayerJobs.PlayerJobs;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClickListener implements Listener {

    private PlayerJobs plugin;

    public InventoryClickListener(PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (inventory != null) {
            String title = inventory.getTitle().substring(2, 7);

            if (title.equals("[Job]")) {

                event.setCancelled(true);
                Player player = ((Player) event.getWhoClicked());

                if (plugin.getOpenMenus().containsKey(player)) {

                    AbstractMenu menu =
                            plugin.getOpenMenus().get(player).getMenu(inventory);

                    if (menu instanceof JobMenu) {

                        menu.click(event);

                    } else if (menu instanceof MenuFilter) {

                        if (event.getCursor().getType() != Material.AIR) {

                            if (event.getCurrentItem() == null) {
                                ((MenuFilter) menu).addButton(
                                        event.getCursor(), event.getSlot());
                            }

                        } else {

                            if (event.getCurrentItem() != null) {

                                if (event.getClick() == ClickType.DROP) {
                                    ((MenuFilter) menu).delButton(event.getSlot());

                                }
                                menu.click(event);
                            }
                        }

                    } else if (menu instanceof MenuEmployees) {

                        if (event.getClick() == ClickType.DROP) {
                            menu.click(event);
                        }
                    } else if (event.getInventory().getTitle().equals(Formatting.inventoryHeading)) {
                        event.setCancelled(false);
                    }
                }
            }
        }
    }
}
