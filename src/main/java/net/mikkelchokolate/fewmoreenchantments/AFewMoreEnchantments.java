package net.mikkelchokolate.fewmoreenchantments;

import net.mikkelchokolate.fewmoreenchantments.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AFewMoreEnchantments {

    public static final String MOD_ID = "few_more_enchantments";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        VeinMiningHandler.register();
        LumberjackHandler.register();
        MagnetismHandler.register();
        HarvestingHandler.register();
        AreaMiningHandler.register();
        DepthMineHandler.register();

        LOGGER.info("A Few More Enchantments initialized!");
    }
}
