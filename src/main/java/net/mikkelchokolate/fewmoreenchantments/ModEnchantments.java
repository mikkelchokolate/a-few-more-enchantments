package net.mikkelchokolate.fewmoreenchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> SOULBOUND = of("soulbound");
    public static final RegistryKey<Enchantment> VEIN_MINING = of("vein_mining");
    public static final RegistryKey<Enchantment> LUMBERJACK = of("lumberjack");
    public static final RegistryKey<Enchantment> MAGNETISM = of("magnetism");
    public static final RegistryKey<Enchantment> HARVESTING = of("harvesting");

    public static final RegistryKey<Enchantment> AREA_MINING = of("area_mining");
    public static final RegistryKey<Enchantment> DEPTH_MINE = of("depth_mine");

    private static RegistryKey<Enchantment> of(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(AFewMoreEnchantments.MOD_ID, name));
    }
}
