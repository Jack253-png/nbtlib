package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.nbt.NBTConstraints;
import com.mcreater.nbtlib.tags.sets.NonNullEntrySet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public class CompoundTag extends Tag<Map<String, Tag<?>>>
        implements Iterable<Map.Entry<String, Tag<?>>>, Comparable<CompoundTag> {
    public CompoundTag(int capacity) {
        super(new HashMap<>(capacity));
    }

    public CompoundTag() {
        this(8);
    }

    public int size() {
        return getValue().size();
    }

    public Tag<?> remove(String key) {
        return getValue().remove(key);
    }

    public void clear() {
        getValue().clear();
    }

    public boolean containsKey(String key) {
        return getValue().containsKey(key);
    }

    public boolean containsValue(Tag<?> value) {
        return getValue().containsValue(value);
    }

    public Collection<Tag<?>> values() {
        return getValue().values();
    }

    public Set<String> keySet() {
        return getValue().keySet();
    }

    public Set<Map.Entry<String, Tag<?>>> entrySet() {
        return new NonNullEntrySet<>(getValue().entrySet());
    }

    public byte getID() {
        return NBTConstraints.COMPOUND_TAG_ID;
    }

    public void forEach(BiConsumer<String, Tag<?>> action) {
        getValue().forEach(action);
    }

    public <C extends Tag<?>> C get(String key, Class<C> type) {
        Tag<?> t = getValue().get(key);
        if (t != null) {
            return type.cast(t);
        }
        return null;
    }

    public Tag<?> get(String key) {
        return getValue().get(key);
    }

    public NumberTag<?> getNumberTag(String key) {
        return (NumberTag<?>) getValue().get(key);
    }

    public Number getNumber(String key) {
        return getNumberTag(key).getValue();
    }

    public ByteTag getByteTag(String key) {
        return get(key, ByteTag.class);
    }

    public ShortTag getShortTag(String key) {
        return get(key, ShortTag.class);
    }

    public IntegerTag getIntegerTag(String key) {
        return get(key, IntegerTag.class);
    }

    public LongTag getLongTag(String key) {
        return get(key, LongTag.class);
    }

    public FloatTag getFloatTag(String key) {
        return get(key, FloatTag.class);
    }

    public DoubleTag getDoubleTag(String key) {
        return get(key, DoubleTag.class);
    }

    public StringTag getStringTag(String key) {
        return get(key, StringTag.class);
    }

    public ByteArrayTag getByteArrayTag(String key) {
        return get(key, ByteArrayTag.class);
    }

    public IntegerArrayTag getIntArrayTag(String key) {
        return get(key, IntegerArrayTag.class);
    }

    public LongArrayTag getLongArrayTag(String key) {
        return get(key, LongArrayTag.class);
    }

    public ListTag<?> getListTag(String key) {
        return get(key, ListTag.class);
    }

    public CompoundTag getCompoundTag(String key) {
        return get(key, CompoundTag.class);
    }

    public boolean getBoolean(String key) {
        Tag<?> t = get(key);
        return t instanceof ByteTag && ((ByteTag) t).asByte() > 0;
    }

    public byte getByte(String key) {
        ByteTag t = getByteTag(key);
        return t == null ? 0 : t.asByte();
    }

    public short getShort(String key) {
        ShortTag t = getShortTag(key);
        return t == null ? 0 : t.asShort();
    }

    public int getInt(String key) {
        IntegerTag t = getIntegerTag(key);
        return t == null ? 0 : t.asInteger();
    }

    public long getLong(String key) {
        LongTag t = getLongTag(key);
        return t == null ? 0 : t.asLong();
    }

    public float getFloat(String key) {
        FloatTag t = getFloatTag(key);
        return t == null ? 0 : t.asFloat();
    }

    public double getDouble(String key) {
        DoubleTag t = getDoubleTag(key);
        return t == null ? 0 : t.asDouble();
    }

    public String getString(String key) {
        StringTag t = getStringTag(key);
        return t == null ? "" : t.getValue();
    }

    public byte[] getByteArray(String key) {
        ByteArrayTag t = getByteArrayTag(key);
        return t == null ? new byte[0] : t.getValue();
    }

    public int[] getIntArray(String key) {
        IntegerArrayTag t = getIntArrayTag(key);
        return t == null ? new int[0] : t.getValue();
    }

    public long[] getLongArray(String key) {
        LongArrayTag t = getLongArrayTag(key);
        return t == null ? new long[0] : t.getValue();
    }

    public Tag<?> put(String key, Tag<?> tag) {
        return getValue().put(Objects.requireNonNull(key), Objects.requireNonNull(tag));
    }

    public Tag<?> putIfNotNull(String key, Tag<?> tag) {
        if (tag == null) {
            return this;
        }
        return put(key, tag);
    }

    public Tag<?> putBoolean(String key, boolean value) {
        return put(key, new ByteTag(value));
    }

    public Tag<?> putByte(String key, byte value) {
        return put(key, new ByteTag(value));
    }

    public Tag<?> putShort(String key, short value) {
        return put(key, new ShortTag(value));
    }

    public Tag<?> putInt(String key, int value) {
        return put(key, new IntegerTag(value));
    }

    public Tag<?> putLong(String key, long value) {
        return put(key, new LongTag(value));
    }

    public Tag<?> putFloat(String key, float value) {
        return put(key, new FloatTag(value));
    }

    public Tag<?> putDouble(String key, double value) {
        return put(key, new DoubleTag(value));
    }

    public Tag<?> putString(String key, String value) {
        return put(key, new StringTag(value));
    }

    public Tag<?> putByteArray(String key, byte[] value) {
        return put(key, new ByteArrayTag(value));
    }

    public Tag<?> putIntArray(String key, int[] value) {
        return put(key, new IntegerArrayTag(value));
    }

    public Tag<?> putLongArray(String key, long[] value) {
        return put(key, new LongArrayTag(value));
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!super.equals(other) || size() != ((CompoundTag) other).size()) {
            return false;
        }
        for (Map.Entry<String, Tag<?>> e : getValue().entrySet()) {
            Tag<?> v;
            if ((v = ((CompoundTag) other).get(e.getKey())) == null || !e.getValue().equals(v)) {
                return false;
            }
        }
        return true;
    }
    public int compareTo(CompoundTag o) {
        return Integer.compare(size(), o.size());
    }

    public Iterator<Map.Entry<String, Tag<?>>> iterator() {
        return entrySet().iterator();
    }

    public CompoundTag clone() {
        CompoundTag tag = new CompoundTag((int) Math.ceil(getValue().size() / 0.75f));
        getValue().forEach(tag::put);
        return tag;
    }
}
