package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.NBTConstraints;

import java.util.Arrays;

public class ByteArrayTag extends ArrayTag<byte[]> implements Comparable<ByteArrayTag> {
    /**
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public ByteArrayTag(byte[] value) {
        super(value);
    }

    public ByteArrayTag() {
        super(new byte[0]);
    }

    public boolean equals(Object other) {
        return super.equals(other) && Arrays.equals(getValue(), ((ByteArrayTag) other).getValue());
    }

    public int hashCode() {
        return Arrays.hashCode(getValue());
    }

    public byte getID() {
        return NBTConstraints.BYTE_ARRAY_TAG_ID;
    }

    public Tag<byte[]> clone() {
        return new ByteArrayTag(Arrays.copyOf(getValue(), length()));
    }

    public int compareTo(ByteArrayTag o) {
        return Integer.compare(length(), o.length());
    }
}
