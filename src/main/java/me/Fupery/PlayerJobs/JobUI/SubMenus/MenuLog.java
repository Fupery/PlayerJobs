package me.Fupery.PlayerJobs.JobUI.SubMenus;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import me.Fupery.PlayerJobs.JobUI.Buttons.AbstractButton;
import me.Fupery.PlayerJobs.JobUI.MenuHandler;
import me.Fupery.PlayerJobs.Utils.Formatting;
import org.bukkit.Bukkit;

public class MenuLog extends AbstractMenu {
    public MenuLog(MenuHandler handler) {
        super(handler);
        menu = Bukkit.createInventory(handler.getPlayer(), 9, Formatting.logMenuHeading);
    }

    @Override
    public void close() {

    }

    @Override
    public void passValues(AbstractButton button, Object value) {

    }
}
