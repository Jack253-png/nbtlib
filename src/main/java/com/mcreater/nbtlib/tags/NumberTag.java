package com.mcreater.nbtlib.tags;

public abstract class NumberTag<T extends Number & Comparable<T>> extends Tag<T> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public NumberTag(T value) {
        super(value);
    }

    public byte asByte() {
        return getValue().byteValue();
    }

    public short asShort() {
        return getValue().shortValue();
    }

    public int asInteger() {
        return getValue().intValue();
    }

    public long asLong() {
        return getValue().longValue();
    }

    public float asFloat() {
        return getValue().floatValue();
    }

    public double asDouble() {
        return getValue().doubleValue();
    }
}
