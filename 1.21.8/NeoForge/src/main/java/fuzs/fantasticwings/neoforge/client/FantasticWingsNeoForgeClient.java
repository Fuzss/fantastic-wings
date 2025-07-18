package fuzs.fantasticwings.neoforge.client;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.client.FantasticWingsClient;
import fuzs.fantasticwings.data.client.ModLanguageProvider;
import fuzs.fantasticwings.data.client.ModModelProvider;
import fuzs.fantasticwings.neoforge.data.client.ModSoundProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = FantasticWings.MOD_ID, dist = Dist.CLIENT)
public class FantasticWingsNeoForgeClient {

    public FantasticWingsNeoForgeClient() {
        ClientModConstructor.construct(FantasticWings.MOD_ID, FantasticWingsClient::new);
        DataProviderHelper.registerDataProviders(FantasticWings.MOD_ID, ModLanguageProvider::new, ModModelProvider::new,
                ModSoundProvider::new
        );
    }
}
