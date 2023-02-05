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

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class NBTOutputStream extends DataOutputStream implements NBTOutput, MaxDepthIO {
    private static final Map<Byte, ExceptionTriConsumer<NBTOutputStream, Tag<?>, Integer, IOException>> writers = new HashMap<>();
    private static final Map<Class<?>, Byte> classIdMapping = new HashMap<>();

    static {
        put(NBTConstraints.END_TAG_ID, NBTOutputStream::writeEndTag, EndTag.class);
        put(NBTConstraints.BYTE_TAG_ID, NBTOutputStream::writeByteTag, ByteTag.class);
        put(NBTConstraints.SHORT_TAG_ID, NBTOutputStream::writeShortTag, ShortTag.class);
        put(NBTConstraints.INTEGER_TAG_ID, NBTOutputStream::writeIntegerTag, IntegerTag.class);
        put(NBTConstraints.LONG_TAG_ID, NBTOutputStream::writeLongTag, LongTag.class);
        put(NBTConstraints.FLOAT_TAG_ID, NBTOutputStream::writeFloatTag, FloatTag.class);
        put(NBTConstraints.DOUBLE_TAG_ID, NBTOutputStream::writeDoubleTag, DoubleTag.class);
        put(NBTConstraints.BYTE_ARRAY_TAG_ID, NBTOutputStream::writeByteArrayTag, ByteArrayTag.class);
        put(NBTConstraints.STRING_TAG_ID, NBTOutputStream::writeStringTag, StringTag.class);
        put(NBTConstraints.LIST_TAG_ID, NBTOutputStream::writeListTag, ListTag.class);
        put(NBTConstraints.COMPOUND_TAG_ID, NBTOutputStream::writeCompoundTag, CompoundTag.class);
        put(NBTConstraints.INTEGER_ARRAY_TAG_ID, NBTOutputStream::writeIntegerArrayTag, IntegerArrayTag.class);
        put(NBTConstraints.LONG_ARRAY_TAG_ID, NBTOutputStream::writeLongArrayTag, LongArrayTag.class);
    }

    private static void writeEndTag(NBTOutputStream stream, Tag<?> tag, int depth) {

    }

    private static void writeByteTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeByte(tag.asTag(ByteTag.class).asByte());
    }

    private static void writeShortTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeShort(tag.asTag(ShortTag.class).asShort());
    }

    private static void writeIntegerTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeInt(tag.asTag(IntegerTag.class).asInteger());
    }

    private static void writeLongTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeLong(tag.asTag(LongTag.class).asLong());
    }

    private static void writeFloatTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeFloat(tag.asTag(FloatTag.class).asFloat());
    }

    private static void writeDoubleTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeDouble(tag.asTag(DoubleTag.class).asDouble());
    }

    private static void writeByteArrayTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeInt(tag.asTag(ByteArrayTag.class).length());
        out.write(tag.asTag(ByteArrayTag.class).getValue());
    }

    private static void writeStringTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeUTF(tag.asTag(StringTag.class).getValue());
    }

    private static void writeListTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.write(idFromClass(((ListTag<?>) tag).getTypeClass()));
        out.writeInt(tag.asTag(ListTag.class).size());
        for (Tag<?> t : ((ListTag<?>) tag)) {
            out.writeRawTag(t, out.checkDepth(depth));
        }
    }

    private static void writeCompoundTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
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

    private static void writeIntegerArrayTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeInt(tag.asTag(IntegerArrayTag.class).length());
        for (int i : tag.asTag(IntegerArrayTag.class).getValue()) {
            out.writeInt(i);
        }
    }

    private static void writeLongArrayTag(NBTOutputStream out, Tag<?> tag, int depth) throws IOException {
        out.writeInt(tag.asTag(LongArrayTag.class).length());
        for (long l : tag.asTag(LongArrayTag.class).getValue()) {
            out.writeLong(l);
        }
    }

    private static void put(byte id, ExceptionTriConsumer<NBTOutputStream, Tag<?>, Integer, IOException> f, Class<?> clazz) {
        writers.put(id, f);
        classIdMapping.put(clazz, id);
    }
    /**
     * Creates a new data output stream to write data to the specified
     * underlying output stream. The counter <code>written</code> is
     * set to zero.
     *
     * @param out the underlying output stream, to be saved for later
     *            use.
     * @see FilterOutputStream#out
     */
    public NBTOutputStream(OutputStream out) {
        super(out);
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
        ExceptionTriConsumer<NBTOutputStream, Tag<?>, Integer, IOException> f;
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
}
