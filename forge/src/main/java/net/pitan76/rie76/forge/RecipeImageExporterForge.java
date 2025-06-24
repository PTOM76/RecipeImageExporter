package net.pitan76.rie76.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.pitan76.rie76.RecipeImageExporter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RecipeImageExporter.MOD_ID)
public class RecipeImageExporterForge {
    public RecipeImageExporterForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EventBuses.registerModEventBus(RecipeImageExporter.MOD_ID, modEventBus);
        new RecipeImageExporter();
    }
}