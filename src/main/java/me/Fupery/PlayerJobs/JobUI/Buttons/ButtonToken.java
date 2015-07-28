package me.Fupery.PlayerJobs.JobUI.Buttons;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ButtonToken extends AbstractButton {

    private boolean doesToggle;

    public ButtonToken(AbstractMenu menu, String[] displayText,
                       Material displayItem, boolean doesToggle) {
        super(menu, displayText, displayItem);
        this.doesToggle = doesToggle;
        returnValue = false;
    }

    @Override
    public void onInventoryClick(AbstractMenu menu, InventoryClickEvent event) {
        if (doesToggle && event.getClick() == ClickType.DROP) {
            boolean deleted = (boolean) returnValue;
            returnValue = !deleted;
            int i = ((boolean) returnValue) ? 0 : 1;
            setAmount(i);
            menu.passValues(this, returnValue);
            menu.update(this);
        }
    }
}
