package fuzs.fantasticwings.client.model.pipeline;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class ForwardingVertexConsumer implements VertexConsumer {
    protected final VertexConsumer vertexConsumer;

    public ForwardingVertexConsumer(VertexConsumer vertexConsumer) {
        this.vertexConsumer = vertexConsumer;
    }

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        this.vertexConsumer.addVertex(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        this.vertexConsumer.setColor(r, g, b, a);
        return this;
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        this.vertexConsumer.setUv(u, v);
        return this;
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        this.vertexConsumer.setUv1(u, v);
        return this;
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        this.vertexConsumer.setUv2(u, v);
        return this;
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        this.vertexConsumer.setNormal(x, y, z);
        return this;
    }
}
