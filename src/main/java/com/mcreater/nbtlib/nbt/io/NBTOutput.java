package com.mcreater.nbtlib.nbt.io;

import com.mcreater.nbtlib.tags.NamedTag;
import com.mcreater.nbtlib.tags.Tag;

import java.io.IOException;

public interface NBTOutput {
    void writeTag(NamedTag tag, int maxDepth) throws IOException;

    void writeTag(Tag<?> tag, int maxDepth) throws IOException;

    void flush() throws IOException;
}
