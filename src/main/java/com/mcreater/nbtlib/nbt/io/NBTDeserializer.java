package com.mcreater.nbtlib.nbt.io;

import com.mcreater.nbtlib.io.Deserializer;
import com.mcreater.nbtlib.tags.NamedTag;
import com.mcreater.nbtlib.tags.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class NBTDeserializer implements Deserializer<NamedTag> {
    private final boolean compressed, littleEndian;
    public NBTDeserializer() {
        this(true);
    }

    public NBTDeserializer(boolean compressed) {
        this(compressed, false);
    }

    public NBTDeserializer(boolean compressed, boolean littleEndian) {
        this.compressed = compressed;
        this.littleEndian = littleEndian;
    }
    public NamedTag fromStream(InputStream stream) throws IOException {
        stream = compressed ? new GZIPInputStream(stream) : stream;
        NBTInput input = littleEndian ? new LittleEndianNBTInputStream(stream) : new NBTInputStream(stream);
        return input.readTag(Tag.DEFAULT_MAX_DEPTH);
    }
}
