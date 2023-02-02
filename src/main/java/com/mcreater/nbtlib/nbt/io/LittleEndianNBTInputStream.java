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

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.mcreater.nbtlib.NBTConstraints.BYTE_ARRAY_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.BYTE_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.COMPOUND_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.DOUBLE_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.END_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.FLOAT_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.INTEGER_ARRAY_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.INTEGER_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.LIST_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.LONG_ARRAY_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.LONG_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.SHORT_TAG_ID;
import static com.mcreater.nbtlib.NBTConstraints.STRING_TAG_ID;

public class LittleEndianNBTInputStream implements DataInput, NBTInput, MaxDepthIO, Closeable {
    private final DataInputStream input;

    private static final Map<Byte, ExceptionBiFunction<LittleEndianNBTInputStream, Integer, ? extends Tag<?>, IOException>> readers = new HashMap<>();
    private static final Map<Byte, Class<?>> idClassMapping = new HashMap<>();

    static {
        put(END_TAG_ID, LittleEndianNBTInputStream::readEndTag, EndTag.class);
        put(BYTE_TAG_ID, LittleEndianNBTInputStream::readByteTag, ByteTag.class);
        put(SHORT_TAG_ID, LittleEndianNBTInputStream::readShortTag, ShortTag.class);
        put(INTEGER_TAG_ID, LittleEndianNBTInputStream::readIntegerTag, IntegerTag.class);
        put(LONG_TAG_ID, LittleEndianNBTInputStream::readLongTag, LongTag.class);
        put(FLOAT_TAG_ID, LittleEndianNBTInputStream::readFloatTag, FloatTag.class);
        put(DOUBLE_TAG_ID, LittleEndianNBTInputStream::readDoubleTag, DoubleTag.class);
        put(BYTE_ARRAY_TAG_ID, LittleEndianNBTInputStream::readByteArrayTag, ByteArrayTag.class);
        put(STRING_TAG_ID, LittleEndianNBTInputStream::readStringTag, StringTag.class);
        put(LIST_TAG_ID, LittleEndianNBTInputStream::readListTag, ListTag.class);
        put(COMPOUND_TAG_ID, LittleEndianNBTInputStream::readCompoundTag, CompoundTag.class);
        put(INTEGER_ARRAY_TAG_ID, LittleEndianNBTInputStream::readIntegerArrayTag, IntegerArrayTag.class);
        put(LONG_ARRAY_TAG_ID, LittleEndianNBTInputStream::readLongArrayTag, LongArrayTag.class);
    }

    /**
     * Creates a DataInputStream that uses the specified
     * underlying InputStream.
     *
     * @param in the specified input stream
     */
    public LittleEndianNBTInputStream(InputStream in) {
        this.input = new DataInputStream(in);
    }

    public LittleEndianNBTInputStream(DataInputStream in) {
        this.input = in;
    }


    private static EndTag readEndTag(LittleEndianNBTInputStream stream, int depth) {
        return EndTag.INSTANCE;
    }

    private static ByteTag readByteTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        return new ByteTag(stream.readByte());
    }

    private static ShortTag readShortTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        return new ShortTag(stream.readShort());
    }

    private static IntegerTag readIntegerTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        return new IntegerTag(stream.readInt());
    }

    private static LongTag readLongTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        return new LongTag(stream.readLong());
    }

    private static FloatTag readFloatTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        return new FloatTag(stream.readFloat());
    }

    private static DoubleTag readDoubleTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        return new DoubleTag(stream.readDouble());
    }

    private static ByteArrayTag readByteArrayTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        ByteArrayTag tag = new ByteArrayTag(new byte[stream.readInt()]);
        stream.readFully(tag.getValue());
        return tag;
    }

    private static IntegerArrayTag readIntegerArrayTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        int length = stream.readInt();
        int[] data = new int[length];
        IntegerArrayTag tag = new IntegerArrayTag(data);
        for (int index = 0; index < length; index++) {
            data[index] = stream.readInt();
        }
        return tag;
    }

    private static LongArrayTag readLongArrayTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        int length = stream.readInt();
        long[] data = new long[length];
        LongArrayTag tag = new LongArrayTag(data);
        for (int index = 0; index < length; index++) {
            data[index] = stream.readLong();
        }
        return tag;
    }

    private static StringTag readStringTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        return new StringTag(stream.readUTF());
    }

    private static ListTag<?> readListTag(LittleEndianNBTInputStream stream, int depth) throws IOException {
        byte listTagType = stream.readByte();
        ListTag<?> listTag = ListTag.createUnchecked(idClassMapping.get(listTagType));
        int length = stream.readInt();
        if (length < 0) length = 0;
        for (int index = 0; index < length; index++) {
            listTag.addUnchecked(stream.readTag(listTagType, MaxDepthIO.checkDepth(depth)));
        }
        return listTag;
    }

    private static CompoundTag readCompoundTag(LittleEndianNBTInputStream stream, int maxDepth) throws IOException {
        CompoundTag comp = new CompoundTag();
        for (int id = stream.readByte() & 0xFF; id != 0; id = stream.readByte() & 0xFF) {
            String key = stream.readUTF();
            Tag<?> element = stream.readTag((byte) id, MaxDepthIO.checkDepth(maxDepth));
            comp.put(key, element);
        }
        return comp;
    }

    private static void put(byte id, ExceptionBiFunction<LittleEndianNBTInputStream, Integer, ? extends Tag<?>, IOException> reader, Class<?> clazz) {
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
        ExceptionBiFunction<LittleEndianNBTInputStream, Integer, ? extends Tag<?>, IOException> f;
        if ((f = readers.get(type)) == null) {
            throw new IOException("invalid tag id \"" + type + "\"");
        }
        return f.accept(this, maxDepth);
    }

    public void readFully(byte[] b) throws IOException {
        input.readFully(b);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        input.readFully(b, off, len);
    }

    public int skipBytes(int n) throws IOException {
        return input.skipBytes(n);
    }

    public boolean readBoolean() throws IOException {
        return input.readBoolean();
    }

    public byte readByte() throws IOException {
        return input.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return input.readUnsignedByte();
    }

    public short readShort() throws IOException {
        return Short.reverseBytes(input.readShort());
    }

    public int readUnsignedShort() throws IOException {
        return Short.toUnsignedInt(Short.reverseBytes(input.readShort()));
    }

    public char readChar() throws IOException {
        return Character.reverseBytes(input.readChar());
    }

    public int readInt() throws IOException {
        return Integer.reverseBytes(input.readInt());
    }

    public long readLong() throws IOException {
        return Long.reverseBytes(input.readLong());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(Integer.reverseBytes(input.readInt()));
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(Long.reverseBytes(input.readLong()));
    }

    @Deprecated
    public String readLine() throws IOException {
        return input.readLine();
    }

    public void close() throws IOException {
        input.close();
    }

    public String readUTF() throws IOException {
        byte[] bytes = new byte[readUnsignedShort()];
        readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
