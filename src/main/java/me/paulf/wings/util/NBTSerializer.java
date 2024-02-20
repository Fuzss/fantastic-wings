package me.paulf.wings.util;

import net.minecraft.nbt.Tag;

public interface NBTSerializer<T, N extends Tag> {
    N serialize(T instance);

    T deserialize(N compound);
}
