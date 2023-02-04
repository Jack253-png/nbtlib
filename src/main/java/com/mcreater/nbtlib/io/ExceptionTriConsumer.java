package com.mcreater.nbtlib.io;

public interface ExceptionTriConsumer<A1, A2, A3, E extends Throwable> {
    void accept(A1 a1, A2 a2, A3 a3) throws E;
}
