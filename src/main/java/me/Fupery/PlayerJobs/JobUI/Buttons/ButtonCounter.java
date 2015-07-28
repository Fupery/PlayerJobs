package me.Fupery.PlayerJobs.JobUI.Buttons;

import me.Fupery.PlayerJobs.JobUI.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ButtonCounter extends AbstractButton {

    private double increment;
    private boolean displayAsAmount;
    private boolean hasMax;
    private double maxDeposit;
    private double initialAmount;

    public ButtonCounter(AbstractMenu menu, String[] displayText,
                         Material displayItem, double value, double increment,
                         double maxDeposit, boolean displayAsAmount, boolean hasMax) {
        super(menu, displayText, displayItem);
        this.increment = increment;
        this.displayAsAmount = displayAsAmount;
        this.hasMax = hasMax;

        if (hasMax) {
            this.maxDeposit = maxDeposit;
            initialAmount = value;
        }

        if (displayAsAmount) {
            setAmount((int) value);

        } else {
            returnValue = value;
        }
    }

    @Override
    public void onInventoryClick(AbstractMenu menu, InventoryClickEvent event) {
        double i = 0, j = ((double) returnValue);

        if (event.isLeftClick()) {
            i = increment;

        } else if (event.isRightClick()) {

            if (j > 0) {
                i = -increment;
            }
        }

        if (hasMax && ((i + j) - initialAmount > maxDeposit)) {
            return;
        }
        returnValue = i + j;
        menu.passValues(this, returnValue);
        menu.update(this);
    }

    public boolean isDisplayAsAmount() {
        return displayAsAmount;
    }

    public double getIncrement() {
        return increment;
    }
}
