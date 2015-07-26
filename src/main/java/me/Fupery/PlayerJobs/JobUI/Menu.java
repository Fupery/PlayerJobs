package me.Fupery.PlayerJobs.JobUI;

import me.Fupery.PlayerJobs.JobUI.Buttons.AbstractButton;

public interface Menu {

    public void close();

    public void passValues(AbstractButton button, Object value);
}
