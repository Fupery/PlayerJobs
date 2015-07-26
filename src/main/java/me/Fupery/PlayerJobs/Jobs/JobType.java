package me.Fupery.PlayerJobs.Jobs;

import org.bukkit.Material;

import java.util.HashMap;

public enum JobType {
    MINER, GATHERER, FARMER, WOODCUTTER, BOUNTY_HUNTER;

    public static HashMap<JobType, Material> jobTypeMap;

    static {
        jobTypeMap = new HashMap<JobType, Material>();
        jobTypeMap.put(MINER, Material.GOLD_PICKAXE);
        jobTypeMap.put(GATHERER, Material.GOLDEN_CARROT);
        jobTypeMap.put(FARMER, Material.GOLD_HOE);
        jobTypeMap.put(WOODCUTTER, Material.GOLD_AXE);
        jobTypeMap.put(BOUNTY_HUNTER, Material.SKULL_ITEM);
    }

    public static JobType getJobType(String string) {
        for (JobType j : JobType.values()) {
            if (j.name().equals(string.toUpperCase())) {
                return j;
            }
        }
        return null;
    }
}
