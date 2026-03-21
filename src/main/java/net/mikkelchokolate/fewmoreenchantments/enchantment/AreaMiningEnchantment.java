package net.mikkelchokolate.fewmoreenchantments.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class AreaMiningEnchantment extends Enchantment {

    public AreaMiningEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.DIGGER, new EquipmentSlot[]{
                EquipmentSlot.MAINHAND
        });
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinPower(int level) {
        return 10 + (level - 1) * 10;
    }

    @Override
    public int getMaxPower(int level) {
        return 40 + (level - 1) * 10;
    }
}
