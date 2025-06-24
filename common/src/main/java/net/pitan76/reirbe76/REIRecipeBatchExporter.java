package net.pitan76.reirbe76;

import net.pitan76.reirbe76.block.Blocks;
import net.pitan76.reirbe76.item.ItemGroups;
import net.pitan76.reirbe76.item.Items;
import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class REIRecipeBatchExporter extends CommonModInitializer {
    public static final String MOD_ID = "reirbe76";
    public static final String MOD_NAME = "REIRecipeBatchExporter";

    public static REIRecipeBatchExporter INSTANCE;
    public static CompatRegistryV2 registry;

    @Override
    public void init() {
        INSTANCE = this;
        registry = super.registry;

        ItemGroups.init();
        Blocks.init();
        Items.init();
    }

    // ----
    /**
     * @param path The path of the id
     * @return The id
     */
    public static CompatIdentifier _id(String path) {
        return CompatIdentifier.of(MOD_ID, path);
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public String getName() {
        return MOD_NAME;
    }
}