package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.NBTConstraints;

public class IntegerTag extends NumberTag<Integer> implements Comparable<IntegerTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public IntegerTag(int value) {
        super(value);
    }

    public IntegerTag() {
        super(0);
    }

    public byte getID() {
        return NBTConstraints.INTEGER_TAG_ID;
    }

    public boolean equals(Object other) {
        return super.equals(other) && asInteger() == ((IntegerTag) other).asInteger();
    }

    public int compareTo(IntegerTag other) {
        return getValue().compareTo(other.getValue());
    }

    public IntegerTag clone() {
        return new IntegerTag(getValue());
    }
}
