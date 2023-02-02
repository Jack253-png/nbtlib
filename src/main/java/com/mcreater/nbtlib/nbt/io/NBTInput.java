package com.mcreater.nbtlib.nbt.io;

import com.mcreater.nbtlib.tags.NamedTag;
import com.mcreater.nbtlib.tags.Tag;

import java.io.IOException;

public interface NBTInput {

    NamedTag readTag(int maxDepth) throws IOException;

    Tag<?> readRawTag(int maxDepth) throws IOException;
}