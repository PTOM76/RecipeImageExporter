package net.pitan76.reirbe76.item;

import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.StackActionResult;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class ExampleItem extends CompatItem {

    public ExampleItem(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public StackActionResult onRightClick(ItemUseEvent e) {
        if (e.isClient()) return e.success();

        Player player = e.getUser();
        player.sendMessage(TextUtil.literal("The item was right-clicked!"));

        return e.success();
    }

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        if (e.isClient()) return e.success();

        Player player = e.getPlayer();
        player.sendMessage(TextUtil.literal("The item was right-clicked on a block!"));

        return e.success();
    }
}
