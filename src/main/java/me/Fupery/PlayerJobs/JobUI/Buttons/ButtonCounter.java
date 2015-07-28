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
        double valueToAdd = 0, currentValue = ((double) returnValue);

        valueToAdd = (event.isShiftClick()) ? increment * 10 : increment;

        if (event.isRightClick()) {

            double i = valueToAdd;
            valueToAdd = -i;

            if (currentValue + valueToAdd < 0) {
                return;
            }
        }

        if (hasMax && ((valueToAdd + currentValue) - initialAmount > maxDeposit)) {
            return;
        }
        returnValue = valueToAdd + currentValue;
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
