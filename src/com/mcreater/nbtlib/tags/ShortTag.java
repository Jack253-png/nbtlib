package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.NBTConstraints;

public class ShortTag extends NumberTag<Short> implements Comparable<ShortTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public ShortTag(short value) {
        super(value);
    }

    public ShortTag() {
        super((short) 0);
    }

    public byte getID() {
        return NBTConstraints.SHORT_TAG_ID;
    }
    public boolean equals(Object other) {
        return super.equals(other) && asShort() == ((ShortTag) other).asShort();
    }

    public Tag<Short> clone() {
        return new ShortTag(getValue());
    }

    public int compareTo(ShortTag other) {
        return getValue().compareTo(other.getValue());
    }
}
