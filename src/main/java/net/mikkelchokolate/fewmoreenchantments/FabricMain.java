package net.mikkelchokolate.fewmoreenchantments;

import net.fabricmc.api.ModInitializer;

public class FabricMain implements ModInitializer {
    @Override
    public void onInitialize() {
        new AFewMoreEnchantments();
    }
}
