package net.pitan76.rie76;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CraftingRecipeOutput {
    private static final int IMAGE_WIDTH = 232;
    private static final int IMAGE_HEIGHT = 108;
    private static final Identifier CRAFTING_TABLE_TEXTURE = new Identifier("textures/gui/container/crafting_table.png");

    public static void exportCraftingRecipesForMod(String modId) {
        MinecraftClient mc = MinecraftClient.getInstance();
        RecipeManager recipeManager = mc.world.getRecipeManager();

        List<RecipeEntry<CraftingRecipe>> recipes = recipeManager.listAllOfType(RecipeType.CRAFTING);

        for (RecipeEntry<CraftingRecipe> recipe : recipes) {
            ItemStack result = recipe.value().getResult(mc.world.getRegistryManager());
            Identifier recipeItemId = Registries.ITEM.getId(result.getItem());
            if (!recipeItemId.getNamespace().equals(modId)) continue;
            
            File exportDir = new File(mc.runDirectory, "recipe_exports/" + modId);
            exportDir.mkdirs();

            String baseName = recipeItemId.getPath();
            File outputFile = getUniqueFile(exportDir, baseName + ".png");

            exportRecipeImage(recipe.value(), outputFile);
        }
    }

    private static File getUniqueFile(File dir, String baseName) {
        int i = 1;
        File file = new File(dir, baseName);
        while (file.exists()) {
            String name = baseName.replace(".png", "") + "_" + i + ".png";
            file = new File(dir, name);
            i++;
        }
        return file;
    }

    private static void exportRecipeImage(CraftingRecipe recipe, File outputFile) {
        MinecraftClient mc = MinecraftClient.getInstance();

        float scale = 2.0f;
        int scaledWidth = (int) (IMAGE_WIDTH * scale);
        int scaledHeight = (int) (IMAGE_HEIGHT * scale);
        Framebuffer framebuffer = new SimpleFramebuffer(scaledWidth, scaledHeight, true, MinecraftClient.IS_SYSTEM_MAC);
        framebuffer.beginWrite(true);

        RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);

        DrawContext drawContext = new DrawContext(mc, mc.getBufferBuilders().getEntityVertexConsumers());
        MatrixStack matrixStack = drawContext.getMatrices();

        // 高解像度化のためにスケーリング
        matrixStack.push();
        Matrix4f projMatrix = new Matrix4f().setOrtho(0.0F, (float)IMAGE_WIDTH, (float)IMAGE_HEIGHT, 0.0F, 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix(projMatrix, VertexSorter.BY_Z);
        matrixStack.translate(0.0, 0.0, -2000.0);

        DiffuseLighting.enableGuiDepthLighting();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int gridX = 0;
        int gridY = 0;
        drawContext.drawTexture(CRAFTING_TABLE_TEXTURE, gridX, gridY, 29, 16, 116, 54);

        ItemStack[][] recipeGrid = getRecipeGrid(recipe);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                ItemStack stack = recipeGrid[row][col];
                if (!stack.isEmpty()) {
                    drawContext.drawItem(stack, gridX + col * 18 + 1, gridY + row * 18 + 1);
                    drawContext.drawItemInSlot(mc.textRenderer, stack, gridX + col * 18 + 1, gridY + row * 18 + 1);
                }
            }
        }

        int resultX = gridX + 90;
        int resultY = gridY + (54 - 26) / 2;

        ItemStack resultStack = recipe.getResult(mc.world.getRegistryManager());
        if (!resultStack.isEmpty()) {
            drawContext.drawItem(resultStack, resultX + 5, resultY + 5);
            drawContext.drawItemInSlot(mc.textRenderer, resultStack, resultX + 5, resultY + 5);
        }

        drawContext.draw();
        matrixStack.pop();

        NativeImage nativeImage = new NativeImage(scaledWidth, scaledHeight, false);
        RenderSystem.bindTexture(framebuffer.getColorAttachment());
        nativeImage.loadFromTextureImage(0, false);
        nativeImage.mirrorVertically();

        framebuffer.delete();
        mc.getFramebuffer().beginWrite(true);

        Util.getIoWorkerExecutor().execute(() -> {
            try {
                NativeImage finalImage = new NativeImage(IMAGE_WIDTH, IMAGE_HEIGHT, false);
                nativeImage.resizeSubRectTo(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, finalImage);
                finalImage.writeTo(outputFile);
                finalImage.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                nativeImage.close();
            }
        });
    }

    private static ItemStack[][] getRecipeGrid(CraftingRecipe recipe) {
        ItemStack[][] grid = new ItemStack[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                grid[row][col] = ItemStack.EMPTY;
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
                    if (!ingredient.isEmpty() && ingredient.getMatchingStacks().length > 0) {
                        grid[row][col] = ingredient.getMatchingStacks()[0];
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
                    if (!ingredient.isEmpty() && ingredient.getMatchingStacks().length > 0) {
                        grid[row][col] = ingredient.getMatchingStacks()[0];
                    }
                }
            }
        }
        return grid;
    }
}