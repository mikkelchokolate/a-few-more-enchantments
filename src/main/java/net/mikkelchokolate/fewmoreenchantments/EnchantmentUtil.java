package net.mikkelchokolate.fewmoreenchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public class EnchantmentUtil {

    public static int getLevel(RegistryKey<Enchantment> key, ItemStack stack, DynamicRegistryManager registryManager) {
        Registry<Enchantment> registry = registryManager.get(RegistryKeys.ENCHANTMENT);
        Optional<RegistryEntry.Reference<Enchantment>> entry = registry.getEntry(key);
        return entry.map(e -> EnchantmentHelper.getLevel(e, stack)).orElse(0);
    }

    public static boolean hasEnchantment(RegistryKey<Enchantment> key, ItemStack stack, DynamicRegistryManager registryManager) {
        return getLevel(key, stack, registryManager) > 0;
    }
}
