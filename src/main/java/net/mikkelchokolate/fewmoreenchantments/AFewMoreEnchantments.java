package net.mikkelchokolate.fewmoreenchantments;

import net.mikkelchokolate.fewmoreenchantments.handler.*;
import net.pitan76.mcpitanlib.api.CommonModInitializer;

public class AFewMoreEnchantments extends CommonModInitializer {

    public static final String MOD_ID = "few_more_enchantments";

    @Override
    public void init() {
        ModEnchantments.register(registry);

        VeinMiningHandler.register();
        LumberjackHandler.register();
        MagnetismHandler.register();
        HarvestingHandler.register();
        AreaMiningHandler.register();
        DepthMineHandler.register();
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public String getName() {
        return "A Few More Enchantments";
    }
}
