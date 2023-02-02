package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.NBTConstraints;

public class StringTag extends Tag<String> implements Comparable<StringTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public StringTag(String value) {
        super(value);
    }

    public StringTag() {
        super("");
    }

    public boolean equals(Object other) {
        return super.equals(other) && getValue().equals(((StringTag) other).getValue());
    }

    public byte getID() {
        return NBTConstraints.STRING_TAG_ID;
    }

    public Tag<String> clone() {
        return new StringTag(getValue());
    }

    public int compareTo(StringTag o) {
        return getValue().compareTo(o.getValue());
    }
}
