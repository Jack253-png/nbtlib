package com.mcreater.nbtlib.tags;

import java.util.HashMap;
import java.util.Map;

import static com.mcreater.nbtlib.NBTConstraints.GSON;
import static com.mcreater.nbtlib.NBTConstraints.toJavaNativeData;

public class NamedTag {

    private String name;
    private Tag<?> tag;

    public NamedTag(String name, Tag<?> tag) {
        this.name = name;
        this.tag = tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(Tag<?> tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public Tag<?> getTag() {
        return tag;
    }

    public String toString() {
        if (name == null || name.equals("")) return tag.toString();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("value", toJavaNativeData(tag));
        return GSON.toJson(map);
    }
}
