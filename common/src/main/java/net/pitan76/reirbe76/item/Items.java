package net.pitan76.reirbe76.item;

import net.minecraft.item.Item;
import net.pitan76.reirbe76.block.Blocks;
import net.pitan76.mcpitanlib.api.item.v2.ItemSettingsBuilder;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;

import static net.pitan76.reirbe76.REIRecipeBatchExporter._id;
import static net.pitan76.reirbe76.REIRecipeBatchExporter.registry;

public class Items {

    public static final ItemSettingsBuilder STANDARD_ITEM_SETTINGS = ItemSettingsBuilder.of()
            .addGroup(ItemGroups.EXAMPLE_ITEMS);

    public static RegistryResult<Item> EXAMPLE_ITEM;
    public static RegistryResult<Item> EXAMPLE_BLOCK;

    public static void init() {
        EXAMPLE_ITEM = registry.registerItem(_id("example_item"), () -> new ExampleItem(STANDARD_ITEM_SETTINGS.build(_id("example_item"))));
        EXAMPLE_BLOCK = registry.registerItem(_id("example_block"), () -> ItemUtil.create(Blocks.EXAMPLE_BLOCK.getOrNull(), STANDARD_ITEM_SETTINGS.build(_id("example_block"))));
    }
}
