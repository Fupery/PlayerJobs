package me.Fupery.PlayerJobs.Listeners;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
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
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    private PlayerJobs plugin;

    public InventoryClickListener(PlayerJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (inventory != null && inventory.getTitle() != null) {
            String title = inventory.getTitle();

            if (title.equals(Formatting.menuHeading)
                    || title.equals(Formatting.logMenuHeading)) {

                event.setCancelled(true);
                Player player = ((Player) event.getWhoClicked());

                if (plugin.getOpenMenus().containsKey(player)) {
                    AbstractMenu menu = plugin.getOpenMenus().get(
                            player).getMenu(inventory);
                    menu.click(event);
                }

            } else if (title.equals(Formatting.filterMenuHeading)) {

                event.setCancelled(true);
                MenuFilter menu = (MenuFilter) plugin.getOpenMenus().get(
                        ((Player) event.getWhoClicked())).getMenu(inventory);

                if (event.getCursor().getType() != Material.AIR) {

                    if (event.getCurrentItem() == null) {
                        menu.addButton(event.getCursor(), event.getSlot());
                    }

                } else {

                    if (event.getCurrentItem() != null) {

                        if (event.getClick() == ClickType.DROP) {
                            menu.delButton(event.getSlot());
                        }
                        menu.click(event);
                    }

                }

            } else if (title.equals(Formatting.employeeMenuHeading)) {
                event.setCancelled(true);
            }
        }
    }
}
