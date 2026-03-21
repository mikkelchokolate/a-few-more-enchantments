package net.mikkelchokolate.fewmoreenchantments.handler;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.mikkelchokolate.fewmoreenchantments.ModEnchantments;

import java.util.HashSet;
import java.util.Set;

public class DepthMineHandler {

    private static final Set<BlockPos> PROCESSING = new HashSet<>();

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient()) return;
            if (!(player instanceof ServerPlayerEntity)) return;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            if (PROCESSING.contains(pos)) return;

            ItemStack tool = player.getMainHandStack();
            int areaLevel = ModEnchantments.getLevel(ModEnchantments.AREA_MINING_ID, tool, world);
            if (areaLevel > 0) return;

            int level = ModEnchantments.getLevel(ModEnchantments.DEPTH_MINE_ID, tool, world);
            if (level <= 0) return;

            Direction facing = getMinedDirection(serverPlayer);
            int depth = 1 + level;

            for (int d = 1; d < depth; d++) {
                BlockPos targetPos = pos.offset(facing, d);
                if (tool.isEmpty()) break;

                PROCESSING.add(targetPos);
                BlockState targetState = world.getBlockState(targetPos);
                if (!targetState.isAir() && targetState.getHardness(world, targetPos) >= 0
                        && tool.isSuitableFor(targetState)) {
                    serverPlayer.interactionManager.tryBreakBlock(targetPos);
                }
                PROCESSING.remove(targetPos);
            }
        });
    }

    private static Direction getMinedDirection(ServerPlayerEntity player) {
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
}
