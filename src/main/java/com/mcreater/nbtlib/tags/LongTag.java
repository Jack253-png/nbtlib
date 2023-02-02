package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.NBTConstraints;

public class LongTag extends NumberTag<Long> implements Comparable<LongTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public LongTag(long value) {
        super(value);
    }

    public LongTag() {
        super(0L);
    }

    public boolean equals(Object other) {
        return super.equals(other) && asLong() == ((LongTag) other).asLong();
    }

    public byte getID() {
        return NBTConstraints.LONG_TAG_ID;
    }

    public LongTag clone() {
        return new LongTag(getValue());
    }

    public int compareTo(LongTag o) {
        return getValue().compareTo(o.getValue());
    }
}
