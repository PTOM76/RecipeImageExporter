package net.pitan76.reirbe76.fabric;

import net.pitan76.reirbe76.REIRecipeBatchExporter;
import net.fabricmc.api.ModInitializer;

public class REIRecipeBatchExporterFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new REIRecipeBatchExporter();
    }
}