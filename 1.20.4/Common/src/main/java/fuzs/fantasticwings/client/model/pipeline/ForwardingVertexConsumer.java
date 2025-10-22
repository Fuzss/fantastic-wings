package fuzs.fantasticwings.client.model.pipeline;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class ForwardingVertexConsumer implements VertexConsumer {
    protected final VertexConsumer vertexConsumer;

    public ForwardingVertexConsumer(VertexConsumer vertexConsumer) {
        this.vertexConsumer = vertexConsumer;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        this.vertexConsumer.vertex(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        this.vertexConsumer.color(r, g, b, a);
        return this;
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        this.vertexConsumer.uv(u, v);
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        this.vertexConsumer.overlayCoords(u, v);
        return this;
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        this.vertexConsumer.uv2(u, v);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        this.vertexConsumer.normal(x, y, z);
        return this;
    }

    @Override
    public void endVertex() {
        this.vertexConsumer.endVertex();
    }

    @Override
    public void defaultColor(int r, int g, int b, int a) {
        this.vertexConsumer.defaultColor(r, g, b, a);
    }

    @Override
    public void unsetDefaultColor() {
        this.vertexConsumer.unsetDefaultColor();
    }
}
