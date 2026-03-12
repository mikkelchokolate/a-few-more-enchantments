package net.mikkelchokolate.fewmoreenchantments.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.mikkelchokolate.fewmoreenchantments.EnchantmentUtil;
import net.mikkelchokolate.fewmoreenchantments.ModEnchantments;

import java.util.List;

public class MagnetismHandler {

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.isSpectator()) continue;

                int maxLevel = getMagnetismLevel(player);
                if (maxLevel <= 0) continue;

                double radius = 3.0 * maxLevel;
                attractItems(player, radius);
            }
        });
    }

    private static int getMagnetismLevel(ServerPlayerEntity player) {
        int maxLevel = 0;
        for (ItemStack armorStack : player.getArmorItems()) {
            if (armorStack.isEmpty()) continue;
            int level = EnchantmentUtil.getLevel(ModEnchantments.MAGNETISM, armorStack, player.getWorld().getRegistryManager());
            if (level > maxLevel) {
                maxLevel = level;
            }
        }
        return maxLevel;
    }

    private static void attractItems(ServerPlayerEntity player, double radius) {
        Box box = new Box(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        List<ItemEntity> items = player.getWorld().getEntitiesByClass(ItemEntity.class, box, item -> {
            if (item.isRemoved()) return false;
            if (item.cannotPickup()) return false;
            return item.squaredDistanceTo(player) <= radius * radius;
        });

        Vec3d playerPos = player.getPos().add(0, 0.5, 0);

        for (ItemEntity item : items) {
            Vec3d direction = playerPos.subtract(item.getPos()).normalize();
            double speed = 0.3;
            item.setVelocity(direction.multiply(speed));
            item.velocityModified = true;
        }
    }
}
