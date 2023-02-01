package com.mcreater.nbtlib.tags;

import java.util.Objects;

public abstract class Tag<T> {
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
     * Overwrite {@link Object#equals(Object obj)} method
     * @param obj the object to be compared
     * @return the result of the comparing
     */
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }

    /**
     * Overwrite {@link Object#hashCode()}
     * @return this tag's content's hash code.
     */
    public int hashCode() {
        return value.hashCode();
    }
}
