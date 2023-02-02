package com.mcreater.nbtlib.io;

public interface ExceptionBiFunction<A1, A2, R, E extends Throwable> {
    R accept(A1 arg1, A2 arg2) throws E;
}
