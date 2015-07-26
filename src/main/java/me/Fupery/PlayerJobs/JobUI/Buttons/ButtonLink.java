package me.Fupery.PlayerJobs.JobUI.Buttons;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ButtonLink extends AbstractButton {

    AbstractMenu linkedMenu;

    public ButtonLink(AbstractMenu menu, String[] displayText, Material displayItem,
                      AbstractMenu linkedMenu) {
        super(menu, displayText, displayItem);
        this.linkedMenu = linkedMenu;
    }

    @Override
    public void onInventoryClick(AbstractMenu menu, InventoryClickEvent event) {
        menu.getHandler().openBranch(linkedMenu);
    }
}
