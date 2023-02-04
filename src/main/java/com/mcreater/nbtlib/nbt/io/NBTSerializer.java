package com.mcreater.nbtlib.nbt.io;

import com.mcreater.nbtlib.io.Serializer;
import com.mcreater.nbtlib.tags.NamedTag;
import com.mcreater.nbtlib.tags.Tag;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class NBTSerializer implements Serializer<NamedTag> {
    private final boolean compressed, littleEndian;
    public NBTSerializer() {
        this(true);
    }

    public NBTSerializer(boolean compressed) {
        this(compressed, false);
    }

    public NBTSerializer(boolean compressed, boolean littleEndian) {
        this.compressed = compressed;
        this.littleEndian = littleEndian;
    }
    public void toStream(NamedTag object, OutputStream out) throws IOException {
        out = compressed ? new GZIPOutputStream(out) : out;
        NBTOutput output = littleEndian ? new LittleEndianNBTOutputStream(out) : new NBTOutputStream(out);
        output.writeTag(object, Tag.DEFAULT_MAX_DEPTH);
        out.flush();
    }
}
