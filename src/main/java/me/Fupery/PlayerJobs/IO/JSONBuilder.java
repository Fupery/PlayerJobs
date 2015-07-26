package me.Fupery.PlayerJobs.IO;

public class JSONBuilder {

    String[] args;

    public JSONBuilder (int args) {
        String json;
        if (args == 1) {
            json = command;
        } else {
            json = command + head + tail;

        }

    }
    public static final String
            command = "/tellraw %s ", head = "[\"\",", tail = "]";

}
