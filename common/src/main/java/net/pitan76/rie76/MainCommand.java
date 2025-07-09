package net.pitan76.rie76;

import net.pitan76.mcpitanlib.api.command.CommandSettings;
import net.pitan76.mcpitanlib.api.command.LiteralCommand;
import net.pitan76.mcpitanlib.api.event.ServerCommandEvent;

public class MainCommand extends LiteralCommand {
    @Override
    public void init(CommandSettings settings) {
        addArgumentCommand("export", new ExportCommand());
        addArgumentCommand("e", new ExportCommand());

        // pukiwikiフォーマットでtxtファイルを出力するコマンド
        addArgumentCommand("pkwktxt", new PkwktxtCommand());
    }

    @Override
    public void execute(ServerCommandEvent e) {
        e.sendSuccess("- /rie76 export <modid> : Export recipes for a specific mod. (alias: /rie76 e) \n" +
                "- /rie76 pkwktxt <lang> : Export recipes in PukiWiki format for a specific language.");
    }
}
