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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            // 材料をカウント
            Map<String, Integer> ingredientCount = new HashMap<>();
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    ItemStack stack = recipeGrid[y][x];
                    if (stack != null && !stack.isEmpty()) {
                        CompatIdentifier ingredientId = ItemUtil.toId(stack.getItem());
                        String itemKey = ingredientId.toString();
                        ingredientCount.put(itemKey, ingredientCount.getOrDefault(itemKey, 0) + 1);
                    }
                }
            }

            // 材料部分のテキスト生成
            StringBuilder materialText = new StringBuilder();
            for (Map.Entry<String, Integer> entry : ingredientCount.entrySet()) {
                String itemKey = entry.getKey();
                int count = entry.getValue();

                // アイテム名を取得
                String itemName;
                if (itemKey.contains(":")) {
                    String[] parts = itemKey.split(":");
                    String namespace = parts[0];
                    String path = parts[1];

                    // CompatIdentifierからアイテムを取得してアイテム名を取得
                    CompatIdentifier id = CompatIdentifier.of(namespace, path);
                    itemName = ItemUtil.getNameAsString(ItemUtil.fromId(id));
                } else {
                    itemName = itemKey;
                }

                if (!materialText.isEmpty()) {
                    materialText.append(" &br; ");
                }

                materialText.append(itemName);
                if (count > 1) {
                    materialText.append("x").append(count);
                }
            }

            // 完成品のアイテム名を取得
            String resultName = ItemUtil.getNameAsString(result.getItem());

            // PukiWiki形式の行を生成
            String recipeId = recipe.id().toString();
            output.append("|~").append(resultName)
                  .append(" |BGCOLOR(#C6C6C6):#img(https://pitan76.github.io/mcrecipe/additionalsmallstairs/")
                  .append(recipeId).append(".png) | ")
                  .append(materialText)
                  .append(" | 説明 |\n");
        }

        LanguageUtil.setLanguage(beforeLang);

        File exportDir = new File(ClientUtil.getRunDirectory(), "rie76/" + modId);
        if (!exportDir.mkdirs() && !exportDir.exists()) {
            // ディレクトリ作成に失敗した場合の処理
            System.err.println("Failed to create export directory: " + exportDir.getAbsolutePath());
            return;
        }

        File outputFile = new File(exportDir, lang + ".txt");
        FileControl.fileWriteContents(outputFile, output.toString());
    }
}
