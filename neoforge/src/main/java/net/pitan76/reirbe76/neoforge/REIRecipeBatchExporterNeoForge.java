package net.pitan76.reirbe76.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.pitan76.reirbe76.REIRecipeBatchExporter;

@Mod(REIRecipeBatchExporter.MOD_ID)
public class REIRecipeBatchExporterNeoForge {
    public REIRecipeBatchExporterNeoForge(ModContainer modContainer) {
        IEventBus eventBus = modContainer.getEventBus();

        new REIRecipeBatchExporter();
    }
}