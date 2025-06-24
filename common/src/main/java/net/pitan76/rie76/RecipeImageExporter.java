package net.pitan76.rie76;

import com.mojang.blaze3d.systems.RenderSystem;
import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.api.command.CommandRegistry;
import net.pitan76.mcpitanlib.api.command.CommandSettings;
import net.pitan76.mcpitanlib.api.command.LiteralCommand;
import net.pitan76.mcpitanlib.api.command.argument.StringCommand;
import net.pitan76.mcpitanlib.api.event.ServerCommandEvent;
import net.pitan76.mcpitanlib.api.event.StringCommandEvent;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;
import net.pitan76.mcpitanlib.api.util.client.ClientUtil;

import java.io.File;

public class RecipeImageExporter extends CommonModInitializer {
    public static final String MOD_ID = "rie76";
    public static final String MOD_NAME = "RecipeImageExporter";

    public static RecipeImageExporter INSTANCE;
    public static CompatRegistryV2 registry;

    @Override
    public void init() {
        INSTANCE = this;
        registry = super.registry;

        CommandRegistry.register("rie76", new LiteralCommand() {

            @Override
            public void init(CommandSettings settings) {
                addArgumentCommand(new StringCommand() {
                    @Override
                    public String getArgumentName() {
                        return "modid";
                    }

                    @Override
                    public void execute(StringCommandEvent e) {
                        String modid = e.getArgument("modid", String.class);
                        if (modid.isEmpty()) {
                            e.sendFailure("Please provide a mod ID to export recipes for.");
                            return;
                        }

                        if (!PlatformUtil.isClient()) return;

                        RenderSystem.recordRenderCall(() -> {
                            CraftingRecipeOutput.exportCraftingRecipesForMod(modid);
                        });

                        File exportDir = new File(ClientUtil.getRunDirectory(), "rie76/" + modid);
                        e.sendSuccess("Exported recipes to \"" + exportDir.toString() + "/\" directory.");
                    }
                });
            }

            @Override
            public void execute(ServerCommandEvent e) {
                e.sendSuccess("Use /rie76 <modid> to export recipes for a specific mod.");
            }
        });
    }

    // ----
    /**
     * @param path The path of the id
     * @return The id
     */
    public static CompatIdentifier _id(String path) {
        return CompatIdentifier.of(MOD_ID, path);
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public String getName() {
        return MOD_NAME;
    }
}