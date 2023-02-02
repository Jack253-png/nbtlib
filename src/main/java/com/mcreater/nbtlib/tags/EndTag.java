package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.NBTConstraints;

public class EndTag extends Tag<Void> {
    public static final EndTag INSTANCE = new EndTag();
    private EndTag() {
        super(null);
    }

    protected Void checkValue(Void value) {
        return value;
    }

    public byte getID() {
        return NBTConstraints.END_TAG_ID;
    }

    public Tag<Void> clone() {
        return INSTANCE;
    }
}
