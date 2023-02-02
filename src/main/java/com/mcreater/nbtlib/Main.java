package com.mcreater.nbtlib;

import com.mcreater.nbtlib.nbt.io.NBTInputStream;
import com.mcreater.nbtlib.tags.Tag;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

public class Main {
    public static void main(String[] args) {
        Map.Entry<Tag<?>, Long> result = runCheckTime(() -> {
            try (NBTInputStream stream = new NBTInputStream(new GZIPInputStream(Files.newInputStream(new File("level.dat").toPath())))) {
                return stream.readTag(Integer.MAX_VALUE).getTag();
            }
            catch (Exception ignored) {}
            return null;
        });
        System.out.println(result.getKey());
        System.out.println(result.getValue());
        long total = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();

        System.out.println(total / 1024 / 1024);
    }
    public static <T> Map.Entry<T, Long> runCheckTime(Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        T content = supplier.get();
        long end = System.currentTimeMillis();
        System.out.printf("Time passed for %s: %dms\n", supplier, end - start);
        return new AbstractMap.SimpleEntry(content, end - start);
    }
}