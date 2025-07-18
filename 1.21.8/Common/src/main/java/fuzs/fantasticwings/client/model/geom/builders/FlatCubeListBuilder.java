package fuzs.fantasticwings.client.model.geom.builders;

import com.google.common.collect.ImmutableList;
import fuzs.fantasticwings.client.model.geom.FlatModelPartCube;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;

import java.util.List;

public class FlatCubeListBuilder extends CubeListBuilder {

    @Override
    public List<CubeDefinition> getCubes() {
        return super.getCubes()
                .stream()
                .map((CubeDefinition cubeDefinition) -> (CubeDefinition) new FlatCubeDefinition(cubeDefinition))
                .collect(ImmutableList.toImmutableList());
    }

    public static CubeListBuilder create() {
        return new FlatCubeListBuilder();
    }

    private static class FlatCubeDefinition extends CubeDefinition {

        public FlatCubeDefinition(CubeDefinition cubeDefinition) {
            super("",
                    cubeDefinition.texCoord.u(),
                    cubeDefinition.texCoord.v(),
                    cubeDefinition.origin.x(),
                    cubeDefinition.origin.y(),
                    cubeDefinition.origin.z(),
                    cubeDefinition.dimensions.x(),
                    cubeDefinition.dimensions.y(),
                    cubeDefinition.dimensions.z(),
                    cubeDefinition.grow,
                    cubeDefinition.mirror,
                    cubeDefinition.texScale.u(),
                    cubeDefinition.texScale.v(),
                    cubeDefinition.visibleFaces);
        }

        @Override
        public ModelPart.Cube bake(int texWidth, int texHeight) {
            return new FlatModelPartCube(this.texCoord.u(),
                    this.texCoord.v(),
                    this.origin.x(),
                    this.origin.y(),
                    this.origin.z(),
                    this.dimensions.x(),
                    this.dimensions.y(),
                    this.dimensions.z(),
                    this.grow.growX,
                    this.grow.growY,
                    this.grow.growZ,
                    this.mirror,
                    texWidth * this.texScale.u(),
                    texHeight * this.texScale.v(),
                    this.visibleFaces);
        }
    }
}
