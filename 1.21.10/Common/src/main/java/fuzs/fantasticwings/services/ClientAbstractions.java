package fuzs.fantasticwings.services;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = ServiceProviderHelper.load(ClientAbstractions.class);

    VertexConsumer createVertexConsumer(VertexConsumer vertexConsumer);
}
