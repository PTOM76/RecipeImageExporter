package net.pitan76.rie76;

import com.mojang.blaze3d.systems.RenderSystem;
import net.pitan76.easyapi.FileControl;
import net.pitan76.mcpitanlib.api.command.CommandSettings;
import net.pitan76.mcpitanlib.api.command.LiteralCommand;
import net.pitan76.mcpitanlib.api.command.argument.StringCommand;
import net.pitan76.mcpitanlib.api.event.ServerCommandEvent;
import net.pitan76.mcpitanlib.api.event.StringCommandEvent;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;
import net.pitan76.mcpitanlib.api.util.client.ClientUtil;

import java.io.File;

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
                        if (lang.isEmpty()) {
                            e.sendFailure("Please provide a language code to export recipes in PukiWiki format.");
                            return;
                        }

                        if (!PlatformUtil.isClient()) return;

                        RenderSystem.recordRenderCall(() -> {
                            CraftingRecipeOutput.exportPukiWiki(lang);
                        });

                        File exportDir = new File(ClientUtil.getRunDirectory(), "rie76/" + lang);
                        e.sendSuccess("Exported recipes in PukiWiki format to \"" + exportDir.toString() + "/\" directory.");
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
                if (modid.isEmpty()) {
                    e.sendFailure("Please provide a mod ID to export recipes for.");
                    return;
                }

                if (!PlatformUtil.isClient()) return;



                FileControl.fileWriteContents(
                        new File(ClientUtil.getRunDirectory(), "rie76/" + modid + ".txt"),
                        CraftingRecipeOutput.exportPukiWiki(modid)
                );
            }
        });
    }

    @Override
    public void execute(ServerCommandEvent e) {
        e.sendSuccess("Use /rie76 pkwktxt <modid> to export recipes in PukiWiki format for a specific mod.");
    }

    public static void outputPkwktxt(String modid, String lang) {
        if (!PlatformUtil.isClient()) return;

        RenderSystem.recordRenderCall(() -> {
            CraftingRecipeOutput.exportPukiWiki(modid, lang);
        });

        File exportDir = new File(ClientUtil.getRunDirectory(), "rie76/" + modid);
        exportDir.mkdirs();

        File outputFile = new File(exportDir, lang + ".txt");
        FileControl.fileWriteContents(outputFile, CraftingRecipeOutput.exportPukiWiki(modid, lang));
    }
}
