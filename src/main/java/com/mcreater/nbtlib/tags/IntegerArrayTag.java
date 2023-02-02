package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.NBTConstraints;

import java.util.Arrays;

public class IntegerArrayTag extends ArrayTag<int[]> implements Comparable<IntegerArrayTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public IntegerArrayTag(int[] value) {
        super(value);
    }

    public IntegerArrayTag() {
        super(new int[0]);
    }

    public boolean equals(Object other) {
        return super.equals(other) && Arrays.equals(getValue(), ((IntegerArrayTag) other).getValue());
    }

    public int hashCode() {
        return Arrays.hashCode(getValue());
    }

    public byte getID() {
        return NBTConstraints.INTEGER_ARRAY_TAG_ID;
    }

    public Tag<int[]> clone() {
        return new IntegerArrayTag(Arrays.copyOf(getValue(), length()));
    }

    public int compareTo(IntegerArrayTag other) {
        return Integer.compare(length(), other.length());
    }
}
