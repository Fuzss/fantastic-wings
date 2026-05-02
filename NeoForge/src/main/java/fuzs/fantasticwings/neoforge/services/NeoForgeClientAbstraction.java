package fuzs.fantasticwings.neoforge.services;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.common.services.ClientAbstractions;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;

public final class NeoForgeClientAbstraction implements ClientAbstractions {

    @Override
    public VertexConsumer createVertexConsumer(VertexConsumer vertexConsumer) {
        return new VertexConsumerWrapper(vertexConsumer) {
            // NO-OP
        };
    }
}
