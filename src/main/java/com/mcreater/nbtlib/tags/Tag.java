package com.mcreater.nbtlib.tags;

import com.mcreater.nbtlib.NBTConstraints;

import java.util.Objects;

import static com.mcreater.nbtlib.NBTConstraints.GSON;

public abstract class Tag<T> implements Cloneable {
    /**
     * The default maximum depth of the NBT structure.
     * */
    public static final int DEFAULT_MAX_DEPTH = 512;
    private T value;

    /**
     *
     * @param value Initializes this tag with some value.If the value equals {@code null}, it will throw a {@link NullPointerException}
     */
    public Tag(T value) {
        setValue(value);
    }

    /**
     * Get tag's id, used for serialization and deserialization.
     * @return this tag's id.
     */
    public abstract byte getID();

    /**
     * Set this tag's value to some value.
     * @param value the value to be set to this tag.
     */
    public void setValue(T value) {
        this.value = checkValue(value);
    }

    /**
     * Check the value to be set.
     * @param value the value to be checked.
     * @return the checked value
     */
    protected T checkValue(T value) {
        return Objects.requireNonNull(value);
    }

    /**
     * Get this tag's value.
     * @return this tag's value.
     */
    public T getValue() {
        return value;
    }

    /**
     * Overwrite {@link Object#equals(Object obj)} method.
     * @param obj the object to be compared
     * @return the result of the comparing
     */
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }

    /**
     * Overwrite {@link Object#hashCode()}.
     * @return this tag's content's hash code.
     */
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Overwrite {@link Object#toString()} method.
     * @return The string representation of the value of this Tag.
     */
    public String toString() {
        return GSON.toJson(NBTConstraints.toJavaNativeData(this));
    }

    /**
     * Creates a clone of this Tag.
     * @return A clone of this Tag.
     * */
    public abstract Tag<T> clone();
}
