package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.nbt.NBTConstraints;

import java.util.Arrays;

public class LongArrayTag extends ArrayTag<long[]> implements Comparable<LongArrayTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public LongArrayTag(long[] value) {
        super(value);
    }

    public LongArrayTag() {
        super(new long[0]);
    }

    public boolean equals(Object other) {
        return super.equals(other) && Arrays.equals(getValue(), ((LongArrayTag) other).getValue());
    }

    public int hashCode() {
        return Arrays.hashCode(getValue());
    }

    public byte getID() {
        return NBTConstraints.LONG_ARRAY_TAG_ID;
    }

    public LongArrayTag clone() {
        return new LongArrayTag(Arrays.copyOf(getValue(), length()));
    }

    public int compareTo(LongArrayTag other) {
        return Integer.compare(length(), other.length());
    }
}
