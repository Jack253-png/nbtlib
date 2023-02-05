package com.mcreater.nbtlib.nbt.io.snbt;

import com.mcreater.nbtlib.io.MaxDepthIO;
import com.mcreater.nbtlib.tags.*;
import com.mcreater.nbtlib.nbt.NBTConstraints;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.regex.Pattern;

public final class SNBTWriter implements MaxDepthIO {

    private static final Pattern NON_QUOTE_PATTERN = Pattern.compile("[a-zA-Z_.+\\-]+");

    private final Writer writer;

    private SNBTWriter(Writer writer) {
        this.writer = writer;
    }

    public static void write(Tag<?> tag, Writer writer, int maxDepth) throws IOException {
        new SNBTWriter(writer).writeAnything(tag, maxDepth);
    }

    public static void write(Tag<?> tag, Writer writer) throws IOException {
        write(tag, writer, Tag.DEFAULT_MAX_DEPTH);
    }

    private void writeAnything(Tag<?> tag, int maxDepth) throws IOException {
        switch (tag.getID()) {
            case NBTConstraints.END_TAG_ID:
                //do nothing
                break;
            case NBTConstraints.BYTE_TAG_ID:
                writer.append(Byte.toString(tag.asTag(ByteTag.class).asByte())).write('b');
                break;
            case NBTConstraints.SHORT_TAG_ID:
                writer.append(Short.toString(tag.asTag(ShortTag.class).asShort())).write('s');
                break;
            case NBTConstraints.INTEGER_TAG_ID:
                writer.write(Integer.toString(tag.asTag(IntegerTag.class).asInteger()));
                break;
            case NBTConstraints.LONG_TAG_ID:
                writer.append(Long.toString(tag.asTag(LongTag.class).asLong())).write('l');
                break;
            case NBTConstraints.FLOAT_TAG_ID:
                writer.append(Float.toString(tag.asTag(FloatTag.class).asFloat())).write('f');
                break;
            case NBTConstraints.DOUBLE_TAG_ID:
                writer.append(Double.toString(tag.asTag(DoubleTag.class).asDouble())).write('d');
                break;
            case NBTConstraints.BYTE_ARRAY_TAG_ID:
                writeArray(tag.asTag(ByteArrayTag.class).getValue(), tag.asTag(ByteArrayTag.class).length(), "B");
                break;
            case NBTConstraints.STRING_TAG_ID:
                writer.write(escapeString(tag.asTag(StringTag.class).getValue()));
                break;
            case NBTConstraints.LIST_TAG_ID:
                writer.write('[');
                for (int i = 0; i < tag.asTag(ListTag.class).size(); i++) {
                    writer.write(i == 0 ? "" : ",");
                    writeAnything(tag.asTag(ListTag.class).get(i), checkDepth(maxDepth));
                }
                writer.write(']');
                break;
            case NBTConstraints.COMPOUND_TAG_ID:
                writer.write('{');
                boolean first = true;
                for (Map.Entry<String, Tag<?>> entry : tag.asTag(CompoundTag.class)) {
                    writer.write(first ? "" : ",");
                    writer.append(escapeString(entry.getKey())).write(':');
                    writeAnything(entry.getValue(), checkDepth(maxDepth));
                    first = false;
                }
                writer.write('}');
                break;
            case NBTConstraints.INTEGER_ARRAY_TAG_ID:
                writeArray(tag.asTag(IntegerArrayTag.class).getValue(), tag.asTag(IntegerArrayTag.class).length(), "I");
                break;
            case NBTConstraints.LONG_ARRAY_TAG_ID:
                writeArray(tag.asTag(LongArrayTag.class).getValue(), tag.asTag(LongArrayTag.class).length(), "L");
                break;
            default:
                throw new IOException("unknown tag with id \"" + tag.getID() + "\"");
        }
    }

    private void writeArray(Object array, int length, String prefix) throws IOException {
        writer.append('[').append(prefix).write(';');
        for (int i = 0; i < length; i++) {
            writer.append(i == 0 ? "" : ",").write(Array.get(array, i).toString());
        }
        writer.write(']');
    }

    public static String escapeString(String s) {
        if (!NON_QUOTE_PATTERN.matcher(s).matches()) {
            StringBuilder sb = new StringBuilder();
            sb.append('"');
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '\\' || c == '"') {
                    sb.append('\\');
                }
                sb.append(c);
            }
            sb.append('"');
            return sb.toString();
        }
        return s;
    }
}