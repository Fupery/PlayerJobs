package me.Fupery.PlayerJobs.JobUI.SubMenus;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import me.Fupery.PlayerJobs.JobUI.Buttons.AbstractButton;
import me.Fupery.PlayerJobs.JobUI.Buttons.ButtonToken;
import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MenuDump extends AbstractMenu {

    public MenuDump(MenuHandler handler) {
        super(handler);
        buttons = new AbstractButton[1];
        buttons[0] = new ButtonToken(this, new String[] {
                ChatColor.YELLOW + "Currently Accepting:"
        }, Material.SIGN, false);
        menu.setItem(0, buttons[0]);
    }

    @Override
    public void close() {

    }

    @Override
    public void passValues(AbstractButton button, Object value) {

    }
}
