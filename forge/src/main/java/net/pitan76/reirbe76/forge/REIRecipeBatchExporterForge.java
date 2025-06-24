package net.pitan76.reirbe76.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.pitan76.reirbe76.REIRecipeBatchExporter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(REIRecipeBatchExporter.MOD_ID)
public class REIRecipeBatchExporterForge {
    public REIRecipeBatchExporterForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EventBuses.registerModEventBus(REIRecipeBatchExporter.MOD_ID, modEventBus);
        new REIRecipeBatchExporter();
    }
}