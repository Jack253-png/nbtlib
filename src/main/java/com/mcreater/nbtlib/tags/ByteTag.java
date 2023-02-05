package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.nbt.NBTConstraints;

public class ByteTag extends NumberTag<Byte> implements Comparable<ByteTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public ByteTag(byte value) {
        super(value);
    }

    public ByteTag() {
        super((byte) 0);
    }

    public ByteTag(boolean value) {
        super((byte) (value ? 1 : 0));
    }

    public boolean asBoolean() {
        return getValue() > 0;
    }

    public byte getID() {
        return NBTConstraints.BYTE_TAG_ID;
    }

    public boolean equals(Object other) {
        return super.equals(other) && asByte() == ((ByteTag) other).asByte();
    }

    public int compareTo(ByteTag other) {
        return getValue().compareTo(other.getValue());
    }

    public ByteTag clone() {
        return new ByteTag(getValue());
    }
}
