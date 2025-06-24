package net.pitan76.rie76.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.pitan76.rie76.RecipeImageExporter;

@Mod(RecipeImageExporter.MOD_ID)
public class RecipeImageExporterNeoForge {
    public RecipeImageExporterNeoForge(ModContainer modContainer) {
        IEventBus eventBus = modContainer.getEventBus();

        new RecipeImageExporter();
    }
}