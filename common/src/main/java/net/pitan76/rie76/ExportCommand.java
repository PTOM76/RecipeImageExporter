package net.pitan76.rie76;

import com.mojang.blaze3d.systems.RenderSystem;
import net.pitan76.mcpitanlib.api.command.CommandSettings;
import net.pitan76.mcpitanlib.api.command.LiteralCommand;
import net.pitan76.mcpitanlib.api.command.argument.StringCommand;
import net.pitan76.mcpitanlib.api.event.ServerCommandEvent;
import net.pitan76.mcpitanlib.api.event.StringCommandEvent;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;
import net.pitan76.mcpitanlib.api.util.client.ClientUtil;

import java.io.File;

public class ExportCommand extends LiteralCommand {
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
                    CraftingRecipeOutput.export(modid);
                });

                File exportDir = new File(ClientUtil.getRunDirectory(), "rie76/" + modid);
                e.sendSuccess("Exported recipes to \"" + exportDir.toString() + "/\" directory.");
            }
        });
    }

    @Override
    public void execute(ServerCommandEvent e) {
        e.sendSuccess("Use /rie76 export <modid> to export recipes for a specific mod.");
    }
}
