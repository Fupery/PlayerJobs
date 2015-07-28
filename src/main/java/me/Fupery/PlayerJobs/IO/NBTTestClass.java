package me.Fupery.PlayerJobs.IO;

import org.bukkit.Bukkit;

import java.io.IOException;

public class NBTTestClass {

    public static void saveNBT() throws IOException {

        NBTTree tree = new NBTTree();


        for (byte b : tree.writeTags()) {
            Bukkit.getLogger().info(b + " ");
        }

//        if (!file.exists()) {
//            file.createNewFile();
//        }
//
//      FileOutputStream fos = new FileOutputStream(file);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        baos.write(tree.writeTags());
//        baos.writeTo(fos);

//        for (byte b: baos.toByteArray()) {
//            Bukkit.getLogger().info(b + " ");
//        }
    }
}
