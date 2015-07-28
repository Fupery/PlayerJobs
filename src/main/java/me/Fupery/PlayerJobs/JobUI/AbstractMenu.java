package me.Fupery.PlayerJobs.JobUI;

import me.Fupery.PlayerJobs.JobUI.Buttons.AbstractButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractMenu implements Menu {

    protected MenuHandler handler;
    protected Inventory menu;

    protected AbstractButton[] buttons;

    protected AbstractMenu(MenuHandler handler) {
        this.handler = handler;
    }

    public void click(InventoryClickEvent event) {

        if (buttons != null && event.getSlot() < buttons.length
                && buttons[event.getSlot()] != null) {
            buttons[event.getSlot()].onInventoryClick(this, event);
        }
    }

    public void open() {
        handler.getPlayer().openInventory(menu);
    }

    public void update(AbstractButton button) {
        new UpdateTask(button, this).runTask(handler.getPlugin());
    }

    public Inventory getInventory() {
        return menu;
    }

    public void setInventory(Inventory inventory) {
        this.menu = inventory;
    }

    public MenuHandler getHandler() {
        return handler;
    }

    public AbstractButton[] getButtons() {
        return buttons;
    }

}

class UpdateTask extends BukkitRunnable {

    private AbstractButton button;
    private AbstractMenu menu;

    public UpdateTask(AbstractButton button, AbstractMenu menu) {
        this.button = button;
        this.menu = menu;

    }

    @Override
    public void run() {
        AbstractButton[] buttons = menu.getButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] != null && buttons[i].equals(button)) {
                menu.getInventory().setItem(i, button);
                break;
            }
        }
    }
}
