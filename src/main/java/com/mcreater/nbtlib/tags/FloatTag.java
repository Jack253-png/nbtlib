package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.nbt.NBTConstraints;

public class FloatTag extends NumberTag<Float> implements Comparable<FloatTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public FloatTag(float value) {
        super(value);
    }

    public FloatTag() {
        super(0F);
    }

    public boolean equals(Object other) {
        return super.equals(other) && getValue().equals(((FloatTag) other).getValue());
    }

    public byte getID() {
        return NBTConstraints.FLOAT_TAG_ID;
    }

    public FloatTag clone() {
        return new FloatTag(getValue());
    }

    public int compareTo(FloatTag o) {
        return getValue().compareTo(o.getValue());
    }
}
