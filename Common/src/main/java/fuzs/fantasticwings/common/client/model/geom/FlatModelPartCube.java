package fuzs.fantasticwings.common.client.model.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.fantasticwings.common.services.ClientAbstractions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.EnumSet;
import java.util.Set;

public class FlatModelPartCube extends ModelPart.Cube {

    public FlatModelPartCube(float texCoordU, float texCoordV, float originX, float originY, float originZ, float dimensionX, float dimensionY, float dimensionZ, float growX, float growY, float growZ, boolean mirror, float texScaleU, float texScaleV, Set<Direction> visibleFaces) {
        this(texCoordU,
                texCoordV,
                texCoordU + dimensionX,
                texCoordV + dimensionY,
                originX,
                originY,
                originZ,
                dimensionX,
                dimensionY,
                dimensionZ,
                growX,
                growY,
                growZ,
                mirror,
                texScaleU,
                texScaleV,
                visibleFaces);
    }

    private FlatModelPartCube(float u1, float v1, float u2, float v2, float originX, float originY, float originZ, float dimensionX, float dimensionY, float dimensionZ, float growX, float growY, float growZ, boolean mirror, float texScaleU, float texScaleV, Set<Direction> visibleFaces) {
        super((int) u1,
                (int) v1,
                originX,
                originY,
                originZ,
                dimensionX,
                dimensionY,
                dimensionZ,
                growX,
                growY,
                growZ,
                mirror,
                texScaleU,
                texScaleV,
                EnumSet.noneOf(Direction.class));

        float x0 = this.minX - growX;
        float x1 = this.maxX + growX;
        float y0 = this.minY - growY;
        float y1 = this.maxY + growY;
        float z0 = this.minZ - growZ;
        float z1 = this.maxZ + growZ;
        float stepX = (x1 - x0) / dimensionX;
        float stepY = (y1 - y0) / dimensionY;

        this.polygons = new ModelPart.Polygon[calculatePolygonCount(visibleFaces, (int) dimensionX, (int) dimensionY)];
        int polygonIndex = 0;

        if (visibleFaces.contains(Direction.NORTH)) {
            this.polygons[polygonIndex++] = new ModelPart.Polygon(createVertices(x0,
                    y0,
                    z0,
                    x1,
                    y1,
                    z0,
                    Direction.NORTH), u1, v1, u2, v2, texScaleU, texScaleV, false, Direction.NORTH);
        }

        if (visibleFaces.contains(Direction.SOUTH)) {
            this.polygons[polygonIndex++] = new ModelPart.Polygon(createVertices(x0,
                    y1,
                    z1,
                    x1,
                    y0,
                    z1,
                    Direction.SOUTH), u1, v2, u2, v1, texScaleU, texScaleV, false, Direction.SOUTH);
        }

        if (visibleFaces.contains(Direction.WEST)) {
            for (int i = 0; i < dimensionX; i++) {
                float x = x0 + i * stepX;
                float u = Mth.lerp(i / dimensionX, u1, u2) + 0.5F;
                this.polygons[polygonIndex++] = new ModelPart.Polygon(createVertices(x,
                        y0,
                        z0,
                        x,
                        y1,
                        z1,
                        Direction.WEST), u, v1, u, v2, texScaleU, texScaleV, false, Direction.WEST);
            }
        }

        if (visibleFaces.contains(Direction.EAST)) {
            for (int i = 0; i < dimensionX; i++) {
                float x = x0 + (i + 1) * stepX;
                float u = Mth.lerp(i / dimensionX, u1, u2) + 0.5F;
                this.polygons[polygonIndex++] = new ModelPart.Polygon(createVertices(x,
                        y1,
                        z0,
                        x,
                        y0,
                        z1,
                        Direction.EAST), u, v2, u, v1, texScaleU, texScaleV, false, Direction.EAST);
            }
        }

        if (visibleFaces.contains(Direction.UP)) {
            for (int i = 0; i < dimensionY; i++) {
                float y = y0 + (i + 1) * stepY;
                float v = Mth.lerp(i / dimensionY, v1, v2) + 0.5F;
                this.polygons[polygonIndex++] = new ModelPart.Polygon(createVertices(x0,
                        y,
                        z0,
                        x1,
                        y,
                        z1,
                        Direction.UP), u1, v, u2, v, texScaleU, texScaleV, false, Direction.UP);
            }
        }

        if (visibleFaces.contains(Direction.DOWN)) {
            for (int i = 0; i < dimensionY; i++) {
                float y = y0 + i * stepY;
                float v = Mth.lerp(i / dimensionY, v1, v2) + 0.5F;
                this.polygons[polygonIndex++] = new ModelPart.Polygon(createVertices(x1,
                        y,
                        z0,
                        x0,
                        y,
                        z1,
                        Direction.DOWN), u2, v, u1, v, texScaleU, texScaleV, false, Direction.DOWN);
            }
        }
    }

    @Override
    public void compile(PoseStack.Pose pose, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        // Sodium breaks the extruded polygons, this is a valid workaround.
        // The idea is adapted from lv-wings (https://github.com/ahasasjeb/lv-wings).
        super.compile(pose,
                ClientAbstractions.INSTANCE.createVertexConsumer(vertexConsumer),
                packedLight,
                packedOverlay,
                color);
    }

    private static int calculatePolygonCount(Set<Direction> visibleFaces, int dimensionX, int dimensionY) {
        int polygonCount = 0;
        if (visibleFaces.contains(Direction.NORTH)) {
            polygonCount++;
        }
        if (visibleFaces.contains(Direction.SOUTH)) {
            polygonCount++;
        }
        if (visibleFaces.contains(Direction.WEST)) {
            polygonCount += dimensionX;
        }
        if (visibleFaces.contains(Direction.EAST)) {
            polygonCount += dimensionX;
        }
        if (visibleFaces.contains(Direction.UP)) {
            polygonCount += dimensionY;
        }
        if (visibleFaces.contains(Direction.DOWN)) {
            polygonCount += dimensionY;
        }
        return polygonCount;
    }

    private static ModelPart.Vertex[] createVertices(float x0, float y0, float z0, float x1, float y1, float z1, Direction direction) {
        ModelPart.Vertex[] vertices = new ModelPart.Vertex[4];
        boolean isVertical = direction.getAxis().isVertical();
        vertices[0] = new ModelPart.Vertex(x1, y0, z0, 0.0F, 0.0F);
        vertices[1] = new ModelPart.Vertex(x0, y0, isVertical ? z0 : z1, 0.0F, 0.0F);
        vertices[2] = new ModelPart.Vertex(x0, y1, z1, 0.0F, 0.0F);
        vertices[3] = new ModelPart.Vertex(x1, y1, isVertical ? z1 : z0, 0.0F, 0.0F);
        return vertices;
    }
}
