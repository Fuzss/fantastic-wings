package fuzs.fantasticwings.fabric.client;

import fuzs.fantasticwings.common.FantasticWings;
import fuzs.fantasticwings.common.client.FantasticWingsClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class FantasticWingsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(FantasticWings.MOD_ID, FantasticWingsClient::new);
    }
}
