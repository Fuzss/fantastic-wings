package fuzs.fantasticwings.fabric;

import fuzs.fantasticwings.common.FantasticWings;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class FantasticWingsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(FantasticWings.MOD_ID, FantasticWings::new);
    }
}
