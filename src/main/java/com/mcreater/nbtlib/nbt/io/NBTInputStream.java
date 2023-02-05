package com.mcreater.nbtlib.nbt.io;

import com.mcreater.nbtlib.io.ExceptionBiFunction;
import com.mcreater.nbtlib.io.MaxDepthIO;
import com.mcreater.nbtlib.tags.ByteArrayTag;
import com.mcreater.nbtlib.tags.ByteTag;
import com.mcreater.nbtlib.tags.CompoundTag;
import com.mcreater.nbtlib.tags.DoubleTag;
import com.mcreater.nbtlib.tags.EndTag;
import com.mcreater.nbtlib.tags.FloatTag;
import com.mcreater.nbtlib.tags.IntegerArrayTag;
import com.mcreater.nbtlib.tags.IntegerTag;
import com.mcreater.nbtlib.tags.ListTag;
import com.mcreater.nbtlib.tags.LongArrayTag;
import com.mcreater.nbtlib.tags.LongTag;
import com.mcreater.nbtlib.tags.NamedTag;
import com.mcreater.nbtlib.tags.ShortTag;
import com.mcreater.nbtlib.tags.StringTag;
import com.mcreater.nbtlib.tags.Tag;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.mcreater.nbtlib.nbt.NBTConstraints.BYTE_ARRAY_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.BYTE_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.COMPOUND_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.DOUBLE_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.END_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.FLOAT_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.INTEGER_ARRAY_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.INTEGER_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.LIST_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.LONG_ARRAY_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.LONG_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.SHORT_TAG_ID;
import static com.mcreater.nbtlib.nbt.NBTConstraints.STRING_TAG_ID;

public class NBTInputStream extends DataInputStream implements NBTInput, MaxDepthIO {
    private static final Map<Byte, ExceptionBiFunction<NBTInputStream, Integer, ? extends Tag<?>, IOException>> readers = new HashMap<>();
    private static final Map<Byte, Class<?>> idClassMapping = new HashMap<>();

    static {
        put(END_TAG_ID, NBTInputStream::readEndTag, EndTag.class);
        put(BYTE_TAG_ID, NBTInputStream::readByteTag, ByteTag.class);
        put(SHORT_TAG_ID, NBTInputStream::readShortTag, ShortTag.class);
        put(INTEGER_TAG_ID, NBTInputStream::readIntegerTag, IntegerTag.class);
        put(LONG_TAG_ID, NBTInputStream::readLongTag, LongTag.class);
        put(FLOAT_TAG_ID, NBTInputStream::readFloatTag, FloatTag.class);
        put(DOUBLE_TAG_ID, NBTInputStream::readDoubleTag, DoubleTag.class);
        put(BYTE_ARRAY_TAG_ID, NBTInputStream::readByteArrayTag, ByteArrayTag.class);
        put(STRING_TAG_ID, NBTInputStream::readStringTag, StringTag.class);
        put(LIST_TAG_ID, NBTInputStream::readListTag, ListTag.class);
        put(COMPOUND_TAG_ID, NBTInputStream::readCompoundTag, CompoundTag.class);
        put(INTEGER_ARRAY_TAG_ID, NBTInputStream::readIntegerArrayTag, IntegerArrayTag.class);
        put(LONG_ARRAY_TAG_ID, NBTInputStream::readLongArrayTag, LongArrayTag.class);
    }

    /**
     * Creates a DataInputStream that uses the specified
     * underlying InputStream.
     *
     * @param in the specified input stream
     */
    public NBTInputStream(InputStream in) {
        super(in);
    }

    private static EndTag readEndTag(NBTInputStream stream, int depth) {
        return EndTag.INSTANCE;
    }

    private static ByteTag readByteTag(NBTInputStream stream, int depth) throws IOException {
        return new ByteTag(stream.readByte());
    }

    private static ShortTag readShortTag(NBTInputStream stream, int depth) throws IOException {
        return new ShortTag(stream.readShort());
    }

    private static IntegerTag readIntegerTag(NBTInputStream stream, int depth) throws IOException {
        return new IntegerTag(stream.readInt());
    }

    private static LongTag readLongTag(NBTInputStream stream, int depth) throws IOException {
        return new LongTag(stream.readLong());
    }

    private static FloatTag readFloatTag(NBTInputStream stream, int depth) throws IOException {
        return new FloatTag(stream.readFloat());
    }

    private static DoubleTag readDoubleTag(NBTInputStream stream, int depth) throws IOException {
        return new DoubleTag(stream.readDouble());
    }

    private static ByteArrayTag readByteArrayTag(NBTInputStream stream, int depth) throws IOException {
        ByteArrayTag tag = new ByteArrayTag(new byte[stream.readInt()]);
        stream.readFully(tag.getValue());
        return tag;
    }

    private static IntegerArrayTag readIntegerArrayTag(NBTInputStream stream, int depth) throws IOException {
        int length = stream.readInt();
        int[] data = new int[length];
        IntegerArrayTag tag = new IntegerArrayTag(data);
        for (int index = 0; index < length; index++) {
            data[index] = stream.readInt();
        }
        return tag;
    }

    private static LongArrayTag readLongArrayTag(NBTInputStream stream, int depth) throws IOException {
        int length = stream.readInt();
        long[] data = new long[length];
        LongArrayTag tag = new LongArrayTag(data);
        for (int index = 0; index < length; index++) {
            data[index] = stream.readLong();
        }
        return tag;
    }

    private static StringTag readStringTag(NBTInputStream stream, int depth) throws IOException {
        return new StringTag(stream.readUTF());
    }

    private static ListTag<?> readListTag(NBTInputStream stream, int depth) throws IOException {
        byte listTagType = stream.readByte();
        ListTag<?> listTag = ListTag.createUnchecked(idClassMapping.get(listTagType));
        int length = stream.readInt();
        if (length < 0) return listTag;
        while (length > 0) {
            listTag.addUnchecked(stream.readTag(listTagType, depth));
            length--;
        }
        return listTag;
    }

    private static CompoundTag readCompoundTag(NBTInputStream stream, int maxDepth) throws IOException {
        CompoundTag comp = new CompoundTag();
        int id = stream.readByte() & 0xFF;
        while (id != 0) {
            String key = stream.readUTF();
            Tag<?> element = stream.readTag((byte) id, stream.checkDepth(maxDepth));
            comp.put(key, element);
            id = stream.readByte() & 0xFF;
        }
        return comp;
    }

    private static void put(byte id, ExceptionBiFunction<NBTInputStream, Integer, ? extends Tag<?>, IOException> reader, Class<?> clazz) {
        readers.put(id, reader);
        idClassMapping.put(id, clazz);
    }
    public NamedTag readTag(int maxDepth) throws IOException {
        byte id = readByte();
        return new NamedTag(readUTF(), readTag(id, maxDepth));
    }

    public Tag<?> readRawTag(int maxDepth) throws IOException {
        byte id = readByte();
        return readTag(id, maxDepth);
    }

    private Tag<?> readTag(byte type, int maxDepth) throws IOException {
        ExceptionBiFunction<NBTInputStream, Integer, ? extends Tag<?>, IOException> f;
        if ((f = readers.get(type)) == null) {
            throw new IOException("invalid tag id \"" + type + "\"");
        }
        return f.accept(this, maxDepth);
    }
}
