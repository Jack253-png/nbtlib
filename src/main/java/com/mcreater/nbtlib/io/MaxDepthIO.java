package com.mcreater.nbtlib.io;

public interface MaxDepthIO {
    default int checkDepth(int depth) {
        if (depth < 0) throw new IllegalArgumentException("Negative depth not allowed.");
        if (depth == 0) throw new IllegalArgumentException("Max depth reached.");
        return --depth;
    }
}
