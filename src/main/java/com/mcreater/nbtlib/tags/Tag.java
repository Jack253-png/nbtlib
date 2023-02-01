package com.mcreater.nbtlib.tags;

import java.util.Objects;

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
        this.value = Objects.requireNonNull(value);
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
        return valueToString();
    }

    /**
     * Call method {@link Tag#valueToString(int maxDepth)} with argument {@link Tag#DEFAULT_MAX_DEPTH}.
     * @return The string representation of the value of this Tag.
     */
    protected String valueToString() {
        return valueToString(DEFAULT_MAX_DEPTH);
    }

    /**
     * Returns a JSON representation of the value of this Tag.
     * @param maxDepth The maximum nesting depth.
     * @return The string representation of the value of this Tag.
     */
    protected abstract String valueToString(int maxDepth);

    /**
     * Creates a clone of this Tag.
     * @return A clone of this Tag.
     * */
    public abstract Tag<T> clone();
}
