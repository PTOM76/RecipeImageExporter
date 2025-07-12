package net.pitan76.rie76;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;
import net.pitan76.easyapi.FileControl;
import net.pitan76.mcpitanlib.api.command.CommandSettings;
import net.pitan76.mcpitanlib.api.command.LiteralCommand;
import net.pitan76.mcpitanlib.api.command.argument.StringCommand;
import net.pitan76.mcpitanlib.api.event.ServerCommandEvent;
import net.pitan76.mcpitanlib.api.event.StringCommandEvent;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;
import net.pitan76.mcpitanlib.api.util.client.ClientUtil;
import net.pitan76.mcpitanlib.api.util.client.LanguageUtil;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;

import java.io.File;
import java.util.List;

public class PkwktxtCommand extends LiteralCommand {
    @Override
    public void init(CommandSettings settings) {
        addArgumentCommand(new StringCommand() {

            @Override
            public void init(CommandSettings settings) {
                addArgumentCommand("lang", new StringCommand() {
                    @Override
                    public void execute(StringCommandEvent e) {
                        String lang = e.getArgument("lang", String.class);
                        String modid = e.getArgument("modid", String.class);
                        outputPkwktxt(modid, lang);
                    }

                    @Override
                    public String getArgumentName() {
                        return "lang";
                    }
                });
            }

            @Override
            public String getArgumentName() {
                return "modid";
            }

            @Override
            public void execute(StringCommandEvent e) {
                String modid = e.getArgument("modid", String.class);
                outputPkwktxt(modid, "ja_jp");
            }
        });
    }

    @Override
    public void execute(ServerCommandEvent e) {
        e.sendSuccess("Use /rie76 pkwktxt <modid> to export recipes in PukiWiki format for a specific mod.");
    }

    public static void outputPkwktxt(String modId, String lang) {
        if (!PlatformUtil.isClient()) return;

        World world = ClientUtil.getWorld();
        RecipeManager recipeManager = world.getRecipeManager();

        List<RecipeEntry<CraftingRecipe>> recipes = recipeManager.listAllOfType(RecipeType.CRAFTING);

        StringBuilder output = new StringBuilder();

        String beforeLang = LanguageUtil.getLanguage();
        LanguageUtil.setLanguage(lang);

        for (RecipeEntry<CraftingRecipe> recipe : recipes) {
            ItemStack result = recipe.value().getResult(world.getRegistryManager());
            ItemStack[][] recipeGrid = CraftingUtil.getRecipeGrid(recipe.value());

            CompatIdentifier itemId = ItemUtil.toId(result.getItem());
            if (!itemId.getNamespace().equals(modId)) continue;


        }

        LanguageUtil.setLanguage(beforeLang);

        File exportDir = new File(ClientUtil.getRunDirectory(), "rie76/" + modId);
        exportDir.mkdirs();

        File outputFile = new File(exportDir, lang + ".txt");
        FileControl.fileWriteContents(outputFile, output.toString());


    }
}
