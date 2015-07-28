package me.Fupery.PlayerJobs.JobUI.Buttons;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractButton extends ItemStack implements MenuButton {

    protected Object returnValue;

    protected AbstractButton(AbstractMenu menu, String[] displayText, Material displayItem) {

        super();

        setType(displayItem);
        setAmount(1);

        List list = Arrays.asList(displayText);
        List lore = list.subList(1, list.size());

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(displayText[0]);
        meta.setLore(lore);
        setItemMeta(meta);
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
