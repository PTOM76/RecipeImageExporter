package net.pitan76.rie76;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CraftingRecipeOutputTest {

    @TempDir
    File tempDir;

    private Method getRecipeGridMethod;
    private Method getUniqueFileMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // Privateメソッドにアクセスするための準備
        getRecipeGridMethod = CraftingRecipeOutput.class.getDeclaredMethod("getRecipeGrid", CraftingRecipe.class);
        getRecipeGridMethod.setAccessible(true);

        getUniqueFileMethod = CraftingRecipeOutput.class.getDeclaredMethod("getUniqueFile", File.class, String.class);
        getUniqueFileMethod.setAccessible(true);
    }

    @Test
    void testGetRecipeGrid_ShapedRecipe() throws Exception {
        // モックのShapedRecipeを作成
        ShapedRecipe mockRecipe = mock(ShapedRecipe.class);

        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(4);
        ingredients.set(0, Ingredient.ofItems(Items.STONE));
        ingredients.set(1, Ingredient.ofItems(Items.COBBLESTONE));
        ingredients.set(2, Ingredient.ofItems(Items.DIRT));
        ingredients.set(3, Ingredient.ofItems(Items.DIAMOND));

        when(mockRecipe.getIngredients()).thenReturn(ingredients);
        when(mockRecipe.getWidth()).thenReturn(2);
        when(mockRecipe.getHeight()).thenReturn(2);

        // テスト対象メソッドの実行
        ItemStack[][] grid = (ItemStack[][]) getRecipeGridMethod.invoke(null, mockRecipe);

        // 結果の検証
        assertAll(
                () -> assertEquals(Items.STONE, grid[0][0].getItem(), "Item at [0][0] should be STONE"),
                () -> assertEquals(Items.COBBLESTONE, grid[0][1].getItem(), "Item at [0][1] should be COBBLESTONE"),
                () -> assertEquals(Items.DIRT, grid[1][0].getItem(), "Item at [1][0] should be DIRT"),
                () -> assertEquals(Items.DIAMOND, grid[1][1].getItem(), "Item at [1][1] should be DIAMOND"),
                () -> assertTrue(grid[0][2].isEmpty(), "Item at [0][2] should be empty"),
                () -> assertTrue(grid[2][2].isEmpty(), "Item at [2][2] should be empty")
        );
    }

    @Test
    void testGetRecipeGrid_ShapelessRecipe() throws Exception {
        // モックのShapelessRecipeを作成
        ShapelessRecipe mockRecipe = mock(ShapelessRecipe.class);
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(2);
        ingredients.set(0, Ingredient.ofItems(Items.APPLE));
        ingredients.set(1, Ingredient.ofItems(Items.GOLDEN_APPLE));

        when(mockRecipe.getIngredients()).thenReturn(ingredients);

        // テスト対象メソッドの実行
        ItemStack[][] grid = (ItemStack[][]) getRecipeGridMethod.invoke(null, mockRecipe);

        // 結果の検証
        assertAll(
                () -> assertEquals(Items.APPLE, grid[0][0].getItem(), "Item at [0][0] should be APPLE"),
                () -> assertEquals(Items.GOLDEN_APPLE, grid[0][1].getItem(), "Item at [0][1] should be GOLDEN_APPLE"),
                () -> assertTrue(grid[0][2].isEmpty(), "Item at [0][2] should be empty"),
                () -> assertTrue(grid[1][0].isEmpty(), "Item at [1][0] should be empty")
        );
    }

    @Test
    void testGetUniqueFile() throws Exception {
        // テスト対象メソッドの実行
        File firstFile = (File) getUniqueFileMethod.invoke(null, tempDir, "test.png");
        assertEquals("test.png", firstFile.getName());

        // 同名ファイルを作成
        assertTrue(firstFile.createNewFile());

        // 再度実行し、連番が付与されることを確認
        File secondFile = (File) getUniqueFileMethod.invoke(null, tempDir, "test.png");
        assertEquals("test_1.png", secondFile.getName());

        assertTrue(secondFile.createNewFile());

        // さらに実行し、次の連番が付与されることを確認
        File thirdFile = (File) getUniqueFile_invoke(null, tempDir, "test.png");
        assertEquals("test_2.png", thirdFile.getName());
    }
}