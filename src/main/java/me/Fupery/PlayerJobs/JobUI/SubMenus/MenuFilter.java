package me.Fupery.PlayerJobs.JobUI.SubMenus;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import me.Fupery.PlayerJobs.JobUI.Buttons.AbstractButton;
import me.Fupery.PlayerJobs.JobUI.Buttons.ButtonCounter;
import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class MenuFilter extends AbstractMenu {

    public MenuFilter(MenuHandler handler) {
        super(handler);
        menu = Bukkit.createInventory(handler.getPlayer(),
                InventoryType.HOPPER, Formatting.filterMenuHeading);
        menu.setMaxStackSize(1);
        buttons = new AbstractButton[5];
        HashMap<Material, Double> filter = handler.getJob().getFilter();
        if (filter != null && filter.size() > 0) {
            int i = 0;

            for (Material m : filter.keySet()) {

                if (m != null) {
                    buttons[i] = new ButtonCounter(this, new String[]{
                            ChatColor.GREEN + "Wage - " + m.name().toLowerCase(),
                            ChatColor.GOLD + "- $" + filter.get(m) +
                                    ChatColor.YELLOW + " per stack.",
                            "The amount that will be payed to",
                            "your employees for every stack",
                            "that they collect for you.",
                            ChatColor.YELLOW + "Left-click to increase wage,",
                            ChatColor.YELLOW + "Right-click to decrease wage",
                            ChatColor.GOLD + "Press Q/drop key to remove"},
                            m, filter.get(m), 0.5, 0, false, false);
                }
                i++;
            }
            menu.setContents(buttons);
        }
    }

    public boolean addButton(ItemStack itemStack, int slot) {

        for (AbstractButton button : buttons) {

            if (button != null && button.getType().equals(
                    itemStack.getType())) {
                return false;
            }
        }
        buttons[slot] = new ButtonCounter(this, new String[]{
                ChatColor.GREEN + "Wage - " + itemStack.getType().name().toLowerCase(),
                ChatColor.GOLD + "- $" + 1 + ChatColor.YELLOW + " per stack",
                "The amount that will be payed to",
                "your employees for every stack",
                "that they collect for you.",
                ChatColor.YELLOW + "Left-click to increase wage,",
                ChatColor.YELLOW + "Right-click to decrease wage",
                ChatColor.GOLD + "Press Q/drop key to remove"},
                itemStack.getType(), 1, 0.5,
                0, false, false);

        menu.setItem(slot, buttons[slot]);
        return true;
    }

    public void delButton(int slot) {
        buttons[slot] = null;
        menu.setItem(slot, null);
    }

    @Override
    public void close() {
        HashMap<Material, Double> filter = new HashMap<>();

        for (AbstractButton button : buttons) {

            if (button != null) {
                double value = ((double) button.getReturnValue());
                filter.put(button.getType(), value);
            }
        }
        handler.getJob().setFilter(filter);
    }

    @Override
    public void passValues(AbstractButton button, Object value) {

        if (button instanceof ButtonCounter) {
            ItemMeta meta = button.getItemMeta();
            List<String> lore = meta.getLore();
            lore.set(0, ChatColor.GOLD + "- $" + value + ChatColor.YELLOW + " per stack.");
            meta.setLore(lore);
            button.setItemMeta(meta);
        }
    }
}
