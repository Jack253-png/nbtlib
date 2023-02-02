package com.mcreater.nbtlib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcreater.nbtlib.io.ExceptionConsumer;
import com.mcreater.nbtlib.tags.CompoundTag;
import com.mcreater.nbtlib.tags.ListTag;
import com.mcreater.nbtlib.tags.Tag;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NBTConstraints {
    public static final byte END_TAG_ID = 0;
    public static final byte BYTE_TAG_ID = 1;
    public static final byte SHORT_TAG_ID = 2;
    public static final byte INTEGER_TAG_ID = 3;
    public static final byte LONG_TAG_ID = 4;
    public static final byte FLOAT_TAG_ID = 5;
    public static final byte DOUBLE_TAG_ID = 6;
    public static final byte BYTE_ARRAY_TAG_ID = 7;
    public static final byte STRING_TAG_ID = 8;
    public static final byte LIST_TAG_ID = 9;
    public static final byte COMPOUND_TAG_ID = 10;
    public static final byte INTEGER_ARRAY_TAG_ID = 11;
    public static final byte LONG_ARRAY_TAG_ID = 12;
    public static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    public static Object toJavaNativeData(Tag<?> tag) {
        switch (tag.getID()) {
            case END_TAG_ID:
                return null;
            case BYTE_TAG_ID:
            case SHORT_TAG_ID:
            case INTEGER_TAG_ID:
            case LONG_TAG_ID:
            case FLOAT_TAG_ID:
            case DOUBLE_TAG_ID:
            case BYTE_ARRAY_TAG_ID:
            case STRING_TAG_ID:
            case INTEGER_ARRAY_TAG_ID:
            case LONG_ARRAY_TAG_ID:
                return tag.getValue();
            case COMPOUND_TAG_ID:
                CompoundTag tag1 = (CompoundTag) tag;
                Map<String, Object> result = new HashMap<>();
                tag1.entrySet().stream()
                        .filter(NBTConstraints::checkNotEndTag)
                        .map(NBTConstraints::toNativeEntry)
                        .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
                return result;
            case LIST_TAG_ID:
                ListTag<Tag<?>> tag2 = (ListTag<Tag<?>>) tag;
                return tag2.getValue().stream()
                        .filter(NBTConstraints::checkNotEndTag)
                        .map(NBTConstraints::toJavaNativeData)
                        .collect(Collectors.toList());
        }
        return null;
    }
    private static boolean checkNotEndTag(Tag<?> tag) {
        return tag.getID() != END_TAG_ID;
    }
    private static boolean checkNotEndTag(Map.Entry<String, Tag<?>> entry) {
        return entry.getValue().getID() != END_TAG_ID;
    }
    private static Map.Entry<String, Object> toNativeEntry(Map.Entry<String, Tag<?>> entry) {
        return new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), toJavaNativeData(entry.getValue()));
    }
}
