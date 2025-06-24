package net.pitan76.rie76.fabric;

import net.pitan76.rie76.RecipeImageExporter;
import net.fabricmc.api.ModInitializer;

public class RecipeImageExporterFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new RecipeImageExporter();
    }
}