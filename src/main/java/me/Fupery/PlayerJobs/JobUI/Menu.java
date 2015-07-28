package me.Fupery.PlayerJobs.JobUI;

import me.Fupery.PlayerJobs.JobUI.Buttons.AbstractButton;

public interface Menu {

    void close();

    void passValues(AbstractButton button, Object value);
}
