package com.mcreater.nbtlib.nbt.io;

import com.mcreater.nbtlib.nbt.NBTConstraints;
import com.mcreater.nbtlib.io.ExceptionTriConsumer;
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
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class LittleEndianNBTOutputStream implements DataOutput, NBTOutput, MaxDepthIO, Closeable {
    private final DataOutputStream output;
    private static final Map<Byte, ExceptionTriConsumer<LittleEndianNBTOutputStream, Tag<?>, Integer, IOException>> writers = new HashMap<>();
    private static final Map<Class<?>, Byte> classIdMapping = new HashMap<>();

    static {
        put(NBTConstraints.END_TAG_ID, LittleEndianNBTOutputStream::writeEndTag, EndTag.class);
        put(NBTConstraints.BYTE_TAG_ID, LittleEndianNBTOutputStream::writeByteTag, ByteTag.class);
        put(NBTConstraints.SHORT_TAG_ID, LittleEndianNBTOutputStream::writeShortTag, ShortTag.class);
        put(NBTConstraints.INTEGER_TAG_ID, LittleEndianNBTOutputStream::writeIntegerTag, IntegerTag.class);
        put(NBTConstraints.LONG_TAG_ID, LittleEndianNBTOutputStream::writeLongTag, LongTag.class);
        put(NBTConstraints.FLOAT_TAG_ID, LittleEndianNBTOutputStream::writeFloatTag, FloatTag.class);
        put(NBTConstraints.DOUBLE_TAG_ID, LittleEndianNBTOutputStream::writeDoubleTag, DoubleTag.class);
        put(NBTConstraints.BYTE_ARRAY_TAG_ID, LittleEndianNBTOutputStream::writeByteArrayTag, ByteArrayTag.class);
        put(NBTConstraints.STRING_TAG_ID, LittleEndianNBTOutputStream::writeStringTag, StringTag.class);
        put(NBTConstraints.LIST_TAG_ID, LittleEndianNBTOutputStream::writeListTag, ListTag.class);
        put(NBTConstraints.COMPOUND_TAG_ID, LittleEndianNBTOutputStream::writeCompoundTag, CompoundTag.class);
        put(NBTConstraints.INTEGER_ARRAY_TAG_ID, LittleEndianNBTOutputStream::writeIntegerArrayTag, IntegerArrayTag.class);
        put(NBTConstraints.LONG_ARRAY_TAG_ID, LittleEndianNBTOutputStream::writeLongArrayTag, LongArrayTag.class);
    }

    private static void writeEndTag(LittleEndianNBTOutputStream stream, Tag<?> tag, int depth) {

    }

    private static void writeByteTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeByte(tag.asTag(ByteTag.class).asByte());
    }

    private static void writeShortTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeShort(tag.asTag(ShortTag.class).asShort());
    }

    private static void writeIntegerTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeInt(tag.asTag(IntegerTag.class).asInteger());
    }

    private static void writeLongTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeLong(tag.asTag(LongTag.class).asLong());
    }

    private static void writeFloatTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeFloat(tag.asTag(FloatTag.class).asFloat());
    }

    private static void writeDoubleTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeDouble(tag.asTag(DoubleTag.class).asDouble());
    }

    private static void writeByteArrayTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeInt(tag.asTag(ByteArrayTag.class).length());
        out.write(tag.asTag(ByteArrayTag.class).getValue());
    }

    private static void writeStringTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeUTF(tag.asTag(StringTag.class).getValue());
    }

    private static void writeListTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.write(idFromClass(((ListTag<?>) tag).getTypeClass()));
        out.writeInt(tag.asTag(ListTag.class).size());
        for (Tag<?> t : ((ListTag<?>) tag)) {
            out.writeRawTag(t, out.checkDepth(depth));
        }
    }

    private static void writeCompoundTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        for (Map.Entry<String, Tag<?>> entry : tag.asTag(CompoundTag.class)) {
            if (entry.getValue().getID() == NBTConstraints.END_TAG_ID) {
                throw new IOException("end tag not allowed");
            }
            out.writeByte(entry.getValue().getID());
            out.writeUTF(entry.getKey());
            out.writeRawTag(entry.getValue(), out.checkDepth(depth));
        }
        out.writeByte(0);
    }

    private static void writeIntegerArrayTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeInt(tag.asTag(IntegerArrayTag.class).length());
        for (int i : tag.asTag(IntegerArrayTag.class).getValue()) {
            out.writeInt(i);
        }
    }

    private static void writeLongArrayTag(LittleEndianNBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeInt(tag.asTag(LongArrayTag.class).length());
        for (long l : tag.asTag(LongArrayTag.class).getValue()) {
            out.writeLong(l);
        }
    }

    private static void put(byte id, ExceptionTriConsumer<LittleEndianNBTOutputStream, Tag<?>, Integer, IOException> f, Class<?> clazz) {
        writers.put(id, f);
        classIdMapping.put(clazz, id);
    }

    public void writeTag(NamedTag tag, int maxDepth) throws IOException {
        writeByte(tag.getTag().getID());
        if (tag.getTag().getID() != 0) {
            writeUTF(tag.getName() == null ? "" : tag.getName());
        }
        writeRawTag(tag.getTag(), maxDepth);
    }

    public void writeTag(Tag<?> tag, int maxDepth) throws IOException {
        writeByte(tag.getID());
        if (tag.getID() != 0) {
            writeUTF("");
        }
        writeRawTag(tag, maxDepth);
    }

    public void writeRawTag(Tag<?> tag, int maxDepth) throws IOException {
        ExceptionTriConsumer<LittleEndianNBTOutputStream, Tag<?>, Integer, IOException> f;
        if ((f = writers.get(tag.getID())) == null) {
            throw new IOException("invalid tag \"" + tag.getID() + "\"");
        }
        f.accept(this, tag, maxDepth);
    }

    static byte idFromClass(Class<?> clazz) {
        Byte id = classIdMapping.get(clazz);
        if (id == null) {
            throw new IllegalArgumentException("unknown Tag class " + clazz.getName());
        }
        return id;
    }
    public LittleEndianNBTOutputStream(OutputStream out) {
        output = new DataOutputStream(out);
    }

    public LittleEndianNBTOutputStream(DataOutputStream out) {
        output = out;
    }

    public void close() throws IOException {
        output.close();
    }

    public void flush() throws IOException {
        output.flush();
    }

    public void write(int b) throws IOException {
        output.write(b);
    }

    public void write(byte[] b) throws IOException {
        output.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        output.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        output.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        output.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        output.writeShort(Short.reverseBytes((short) v));
    }

    public void writeChar(int v) throws IOException {
        output.writeChar(Character.reverseBytes((char) v));
    }

    public void writeInt(int v) throws IOException {
        output.writeInt(Integer.reverseBytes(v));
    }

    public void writeLong(long v) throws IOException {
        output.writeLong(Long.reverseBytes(v));
    }

    public void writeFloat(float v) throws IOException {
        output.writeInt(Integer.reverseBytes(Float.floatToIntBits(v)));
    }

    public void writeDouble(double v) throws IOException {
        output.writeLong(Long.reverseBytes(Double.doubleToLongBits(v)));
    }

    public void writeBytes(String s) throws IOException {
        output.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        output.writeChars(s);
    }

    public void writeUTF(String s) throws IOException {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeShort(bytes.length);
        write(bytes);
    }
}
