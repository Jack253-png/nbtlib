package com.mcreater.nbtlib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import com.mcreater.nbtlib.tags.NumberTag;

public class NBTConstraints {
    public static final byte END_TAG_ID = 0;
    public static final byte BYTE_TAG_ID = 1;
    public static final byte SHORT_TAG_ID = 2;
    public static final byte INTEGER_TAG_ID = 3;
    public static final byte LONG_TAG_ID = 4;
    public static final byte FLOAT_TAG_ID = 5;
    public static final byte DOUBLE_TAG_ID = 6;
    public static final byte BYTE_ARRAY_TAG_ID = 7;
    public static final byte STRING_TAG_ID = 8;
    public static final byte LONG_ARRAY_TAG_ID = 12;
    public static final byte INTEGER_ARRAY_TAG_ID = 13;
    public static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(NumberTag.class, (JsonSerializer<NumberTag<?>>) (src, typeOfSrc, context) -> context.serialize(src.getValue()))
                    .create();
}
