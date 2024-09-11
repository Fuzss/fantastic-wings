package fuzs.fantasticwings.neoforge;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.data.ModItemTagProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(FantasticWings.MOD_ID)
public class FantasticWingsNeoForge {

    public FantasticWingsNeoForge() {
        ModConstructor.construct(FantasticWings.MOD_ID, FantasticWings::new);
        DataProviderHelper.registerDataProviders(FantasticWings.MOD_ID, ModItemTagProvider::new);
    }
}
