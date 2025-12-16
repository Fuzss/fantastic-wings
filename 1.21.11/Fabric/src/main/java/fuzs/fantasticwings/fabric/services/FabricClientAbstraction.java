package fuzs.fantasticwings.fabric.services;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.services.ClientAbstractions;

public final class FabricClientAbstraction implements ClientAbstractions {

    @Override
    public VertexConsumer createVertexConsumer(VertexConsumer vertexConsumer) {
        return new VertexConsumer() {
            @Override
            public VertexConsumer addVertex(float x, float y, float z) {
                vertexConsumer.addVertex(x, y, z);
                return this;
            }

            @Override
            public VertexConsumer setColor(int r, int g, int b, int a) {
                vertexConsumer.setColor(r, g, b, a);
                return this;
            }

            @Override
            public VertexConsumer setColor(int color) {
                vertexConsumer.setColor(color);
                return this;
            }

            @Override
            public VertexConsumer setUv(float u, float v) {
                vertexConsumer.setUv(u, v);
                return this;
            }

            @Override
            public VertexConsumer setUv1(int u, int v) {
                vertexConsumer.setUv1(u, v);
                return this;
            }

            @Override
            public VertexConsumer setUv2(int u, int v) {
                vertexConsumer.setUv2(u, v);
                return this;
            }

            @Override
            public VertexConsumer setNormal(float x, float y, float z) {
                vertexConsumer.setNormal(x, y, z);
                return this;
            }

            @Override
            public VertexConsumer setLineWidth(float lineWidth) {
                vertexConsumer.setLineWidth(lineWidth);
                return this;
            }
        };
    }
}
