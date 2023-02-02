package com.mcreater.nbtlib.io;

public interface ExceptionConsumer<T, E extends Throwable> {
    void accept(T t) throws E;
}
