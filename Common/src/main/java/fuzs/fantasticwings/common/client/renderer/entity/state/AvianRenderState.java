package fuzs.fantasticwings.common.client.renderer.entity.state;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import net.minecraft.world.phys.Vec3;

public class AvianRenderState {
    public final Int2ObjectFunction<Vec3> wingAngles = new Int2ObjectArrayMap<>();
    public final Int2ObjectFunction<Vec3> featherAngles = new Int2ObjectArrayMap<>();
}
