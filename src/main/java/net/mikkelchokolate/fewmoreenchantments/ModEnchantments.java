package net.mikkelchokolate.fewmoreenchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.enchantment.CompatEnchantment;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.EnchantmentUtil;

public class ModEnchantments {

    private static final String PKG = "net.mikkelchokolate.fewmoreenchantments.enchantment.";

    public static final CompatIdentifier SOULBOUND_ID =
            new CompatIdentifier(AFewMoreEnchantments.MOD_ID, "soulbound");
    public static final CompatIdentifier VEIN_MINING_ID =
            new CompatIdentifier(AFewMoreEnchantments.MOD_ID, "vein_mining");
    public static final CompatIdentifier LUMBERJACK_ID =
            new CompatIdentifier(AFewMoreEnchantments.MOD_ID, "lumberjack");
    public static final CompatIdentifier MAGNETISM_ID =
            new CompatIdentifier(AFewMoreEnchantments.MOD_ID, "magnetism");
    public static final CompatIdentifier HARVESTING_ID =
            new CompatIdentifier(AFewMoreEnchantments.MOD_ID, "harvesting");
    public static final CompatIdentifier AREA_MINING_ID =
            new CompatIdentifier(AFewMoreEnchantments.MOD_ID, "area_mining");
    public static final CompatIdentifier DEPTH_MINE_ID =
            new CompatIdentifier(AFewMoreEnchantments.MOD_ID, "depth_mine");

    public static void register(CompatRegistryV2 registry) {
        // Suppliers are only invoked on 1.18.2–1.20.4.
        // On 1.20.5+ registerEnchantment is a no-op; enchantments are data-driven.
        // We use reflection so that the enchantment subclasses (which extend the
        // abstract Enchantment on 1.18.2) are never directly referenced in this
        // class's bytecode — avoiding IncompatibleClassChangeError on 1.20.5+
        // where Enchantment became a final record.
        registry.registerEnchantment(SOULBOUND_ID,   () -> create("SoulboundEnchantment"));
        registry.registerEnchantment(VEIN_MINING_ID, () -> create("VeinMiningEnchantment"));
        registry.registerEnchantment(LUMBERJACK_ID,  () -> create("LumberjackEnchantment"));
        registry.registerEnchantment(MAGNETISM_ID,   () -> create("MagnetismEnchantment"));
        registry.registerEnchantment(HARVESTING_ID,  () -> create("HarvestingEnchantment"));
        registry.registerEnchantment(AREA_MINING_ID, () -> create("AreaMiningEnchantment"));
        registry.registerEnchantment(DEPTH_MINE_ID,  () -> create("DepthMineEnchantment"));
    }

    private static CompatEnchantment create(String simpleName) {
        try {
            Enchantment e = (Enchantment) Class.forName(PKG + simpleName)
                    .getDeclaredConstructor()
                    .newInstance();
            return new CompatEnchantment(e);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("Failed to instantiate enchantment: " + simpleName, ex);
        }
    }

    public static int getLevel(CompatIdentifier id, ItemStack stack, World world) {
        return EnchantmentUtil.getLevel(EnchantmentUtil.getEnchantment(id), stack, world);
    }

    public static boolean hasEnchantment(CompatIdentifier id, ItemStack stack, World world) {
        return getLevel(id, stack, world) > 0;
    }
}
