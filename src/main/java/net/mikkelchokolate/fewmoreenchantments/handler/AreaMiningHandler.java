package net.mikkelchokolate.fewmoreenchantments.handler;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.mikkelchokolate.fewmoreenchantments.EnchantmentUtil;
import net.mikkelchokolate.fewmoreenchantments.ModEnchantments;

import java.util.HashSet;
import java.util.Set;

public class AreaMiningHandler {

    private static final Set<BlockPos> PROCESSING = new HashSet<>();

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient()) return;
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
            if (PROCESSING.contains(pos)) return;

            ItemStack tool = player.getMainHandStack();
            int radius = getAreaRadius(tool, world);
            if (radius <= 0) return;

            Direction facing = getMinedFace(serverPlayer);
            Set<BlockPos> toBreak = getAreaPositions(pos, facing, radius);

            for (BlockPos targetPos : toBreak) {
                if (tool.isEmpty()) break;
                PROCESSING.add(targetPos);
                BlockState targetState = world.getBlockState(targetPos);
                if (!targetState.isAir() && targetState.getHardness(world, targetPos) >= 0
                        && tool.isSuitableFor(targetState)) {
                    world.breakBlock(targetPos, true, player);
                    tool.damage(1, serverPlayer, EquipmentSlot.MAINHAND);
                }
                PROCESSING.remove(targetPos);
            }
        });
    }

    private static int getAreaRadius(ItemStack tool, World world) {
        int level = EnchantmentUtil.getLevel(ModEnchantments.AREA_MINING, tool, world.getRegistryManager());
        return level;
    }

    private static Direction getMinedFace(ServerPlayerEntity player) {
        Vec3d look = player.getRotationVector();
        double ax = Math.abs(look.x);
        double ay = Math.abs(look.y);
        double az = Math.abs(look.z);

        if (ay >= ax && ay >= az) {
            return look.y > 0 ? Direction.UP : Direction.DOWN;
        } else if (ax >= az) {
            return look.x > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return look.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    private static Set<BlockPos> getAreaPositions(BlockPos center, Direction facing, int radius) {
        Set<BlockPos> positions = new HashSet<>();

        Direction axis1;
        Direction axis2;

        switch (facing.getAxis()) {
            case Y:
                axis1 = Direction.EAST;
                axis2 = Direction.SOUTH;
                break;
            case X:
                axis1 = Direction.UP;
                axis2 = Direction.SOUTH;
                break;
            case Z:
            default:
                axis1 = Direction.EAST;
                axis2 = Direction.UP;
                break;
        }

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if (i == 0 && j == 0) continue;
                BlockPos offset = center
                        .offset(axis1, i)
                        .offset(axis2, j);
                positions.add(offset);
            }
        }

        return positions;
    }
}
