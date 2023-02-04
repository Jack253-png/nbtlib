package com.mcreater.nbtlib;

import com.mcreater.nbtlib.nbt.io.NBTInputStream;
import com.mcreater.nbtlib.tags.NamedTag;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

public class Main {
    public static void main(String[] args) {
        NamedTag tag = runCheckTime(() -> {
            try (NBTInputStream in = new NBTInputStream(new GZIPInputStream(Files.newInputStream(new File("level.dat").toPath())))) {
                return in.readTag(Integer.MAX_VALUE);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
        System.out.println(tag);

        long total = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
        System.out.println(total / 1024 / 1024);
    }
    public static <T> T runCheckTime(Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        T content = supplier.get();
        long end = System.currentTimeMillis();
        System.out.printf("Time passed for %s: %dms\n", supplier, end - start);
        return content;
    }
}