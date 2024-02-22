package fuzs.fantasticwings.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicInteger;

public final class Model3DTexture extends ModelPart.Cube {

    private Model3DTexture(float posX, float posY, float posZ, int width, int height, float u1, float v1, float u2, float v2, int textureWidth, int textureHeight) {
        super(0,
                0,
                posX,
                posY,
                posZ,
                0,
                0,
                0,
                0.0F,
                0.0F,
                0.0F,
                false,
                textureWidth,
                textureHeight,
                EnumSet.allOf(Direction.class)
        );
        int faceCount = 2 + 2 * width + 2 * height;
        ModelPart.Polygon[] polygons = new ModelPart.Polygon[faceCount];
        float x0 = this.minX;
        float x1 = (this.minX + width);
        float y0 = this.minY;
        float y1 = (this.minY + height);
        float z0 = this.minZ;
        float z1 = (this.minZ + 1);
        FaceAdder faces = getFaceAdder(polygons);
        faces.add(x0, y0, z0, x1, y1, z0, u1, v1, u2, v2, Direction.NORTH);
        faces.add(x0, y1, z1, x1, y0, z1, u1, v2, u2, v1, Direction.SOUTH);
        float f5 = 0.5F * (u1 - u2) / width;
        float f6 = 0.5F * (v1 - v2) / height;
        for (int k = 0; k < width; k++) {
            float f7 = x0 + k;
            float f8 = u1 + (u2 - u1) * ((float) k / width) - f5;
            faces.add(f7, y0, z0, f7, y1, z1, f8, v1, f8, v2, Direction.WEST);
        }
        for (int k = 0; k < width; k++) {
            float f8 = u1 + (u2 - u1) * ((float) k / width) - f5;
            float f9 = x0 + (k + 1);
            faces.add(f9, y1, z0, f9, y0, z1, f8, v2, f8, v1, Direction.EAST);
        }
        for (int k = 0; k < height; k++) {
            float f8 = v1 + (v2 - v1) * ((float) k / height) - f6;
            float f9 = y0 + (k + 1);
            faces.add(x0, f9, z0, x1, f9, z1, u1, f8, u2, f8, Direction.UP);
        }
        for (int k = 0; k < height; k++) {
            float f7 = y0 + k;
            float f8 = v1 + (v2 - v1) * ((float) k / height) - f6;
            faces.add(x1, f7, z0, x0, f7, z1, u2, f8, u1, f8, Direction.DOWN);
        }
        this.polygons = polygons;
    }

    private static FaceAdder getFaceAdder(ModelPart.Polygon[] polygons) {
        AtomicInteger polygonIndex = new AtomicInteger();
        return (float x0, float y0, float z0, float x1, float y1, float z1, float u1, float v1, float u2, float v2, Direction normal) -> {
            ModelPart.Vertex[] vertices = new ModelPart.Vertex[4];
            boolean vertical = normal.getAxis().isVertical();
            vertices[0] = new ModelPart.Vertex(x1, y0, z0, 0.0F, 0.0F);
            vertices[1] = new ModelPart.Vertex(x0, y0, vertical ? z0 : z1, 0.0F, 0.0F);
            vertices[2] = new ModelPart.Vertex(x0, y1, z1, 0.0F, 0.0F);
            vertices[3] = new ModelPart.Vertex(x1, y1, vertical ? z1 : z0, 0.0F, 0.0F);
            polygons[polygonIndex.getAndIncrement()] = new ModelPart.Polygon(vertices,
                    u1,
                    v1,
                    u2,
                    v2,
                    64,
                    64,
                    false,
                    normal
            );
        };
    }

    @FunctionalInterface
    interface FaceAdder {

        void add(float x, float y, float z, float x2, float y2, float z2, float u1, float v1, float u2, float v2, Direction normal);
    }

    public static Model3DTexture create(float posX, float posY, float posZ, int width, int height, int u, int v, int textureWidth, int textureHeight) {
        return new Model3DTexture(posX,
                posY,
                posZ,
                width,
                height,
                (float) u,
                (float) v,
                (float) (u + width),
                (float) (v + height),
                textureWidth,
                textureHeight
        );
    }
}
