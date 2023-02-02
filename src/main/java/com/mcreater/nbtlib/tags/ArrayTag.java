package com.mcreater.nbtlib.tags;

import java.lang.reflect.Array;

public abstract class ArrayTag<T> extends Tag<T> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public ArrayTag(T value) {
        super(value);
        if (!value.getClass().isArray()) throw new UnsupportedOperationException("Tag's content must be array.");
    }

    public int length() {
        return Array.getLength(getValue());
    }
}
