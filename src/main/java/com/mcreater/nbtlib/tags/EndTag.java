package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.nbt.NBTConstraints;

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

    public EndTag clone() {
        return INSTANCE;
    }
}
