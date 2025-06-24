package net.pitan76.reirbe76.block;

import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class ExampleBlock extends CompatBlock {

    public ExampleBlock(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        if (e.isSneaking()) return super.onRightClick(e);
        if (e.isClient()) return e.success();

        Player player = e.getPlayer();
        player.sendMessage(TextUtil.literal("The block was right-clicked!"));

        return e.success();
    }
}
