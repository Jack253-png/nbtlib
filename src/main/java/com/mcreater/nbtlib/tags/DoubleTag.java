package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.nbt.NBTConstraints;

public class DoubleTag extends NumberTag<Double> implements Comparable<DoubleTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public DoubleTag(double value) {
        super(value);
    }

    public DoubleTag() {
        super(0D);
    }

    public boolean equals(Object other) {
        return super.equals(other) && getValue().equals(((DoubleTag) other).getValue());
    }

    public byte getID() {
        return NBTConstraints.DOUBLE_TAG_ID;
    }

    public DoubleTag clone() {
        return new DoubleTag(getValue());
    }

    public int compareTo(DoubleTag o) {
        return getValue().compareTo(o.getValue());
    }
}
