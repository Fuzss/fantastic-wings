package fuzs.fantasticwings.neoforge;

import fuzs.fantasticwings.FantasticWings;
import fuzs.fantasticwings.data.ModItemTagProvider;
import fuzs.fantasticwings.data.client.ModLanguageProvider;
import fuzs.fantasticwings.data.client.ModModelProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(FantasticWings.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FantasticWingsNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(FantasticWings.MOD_ID, FantasticWings::new);
        DataProviderHelper.registerDataProviders(FantasticWings.MOD_ID,
                ModItemTagProvider::new,
                ModLanguageProvider::new,
                ModModelProvider::new
        );
    }
}
