package net.pitan76.rie76;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.pitan76.mcpitanlib.api.util.IngredientUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

import java.util.List;

public class CraftingUtil {
    /**
     * レシピの3x3グリッドに配置されたItemStackを取得する
     * @param recipe CraftingRecipe
     * @return 3x3のItemStack配列
     */
    public static ItemStack[][] getRecipeGrid(CraftingRecipe recipe) {
        ItemStack[][] grid = new ItemStack[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                grid[row][col] = ItemStackUtil.empty();
            }
        }

        if (recipe instanceof ShapedRecipe shapedRecipe) {
            int width = shapedRecipe.getWidth();
            List<Ingredient> ingredients = shapedRecipe.getIngredients();
            for (int i = 0; i < ingredients.size(); i++) {
                int row = i / width;
                int col = i % width;
                if (row < 3 && col < 3) {
                    Ingredient ingredient = ingredients.get(i);
                    if (!ingredient.isEmpty() && IngredientUtil.getMatchingStacks(ingredient).length > 0) {
                        grid[row][col] = IngredientUtil.getMatchingStacks(ingredient)[0];
                    }
                }
            }
        } else if (recipe instanceof ShapelessRecipe) {
            List<Ingredient> ingredients = recipe.getIngredients();
            for (int i = 0; i < ingredients.size(); i++) {
                int row = i / 3;
                int col = i % 3;
                if (row < 3) {
                    Ingredient ingredient = ingredients.get(i);
                    if (!ingredient.isEmpty() && IngredientUtil.getMatchingStacks(ingredient).length > 0) {
                        grid[row][col] = IngredientUtil.getMatchingStacks(ingredient)[0];
                    }
                }
            }
        }
        return grid;
    }
}
