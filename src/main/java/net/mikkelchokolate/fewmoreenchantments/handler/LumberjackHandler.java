package net.mikkelchokolate.fewmoreenchantments.handler;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mikkelchokolate.fewmoreenchantments.ModEnchantments;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class LumberjackHandler {

    private static final Set<BlockPos> PROCESSING = new HashSet<>();

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient()) return;
            if (!(player instanceof ServerPlayerEntity)) return;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            ItemStack tool = player.getMainHandStack();
            int level = ModEnchantments.getLevel(ModEnchantments.LUMBERJACK_ID, tool, world);
            if (level <= 0) return;

            if (!isLog(state)) return;
            if (PROCESSING.contains(pos)) return;

            int maxBlocks = 5 + (level * 10);
            Set<BlockPos> toBreak = findConnectedLogs(world, pos, maxBlocks);

            for (BlockPos targetPos : toBreak) {
                if (tool.isEmpty()) break;
                PROCESSING.add(targetPos);
                BlockState targetState = world.getBlockState(targetPos);
                if (!targetState.isAir()) {
                    world.breakBlock(targetPos, true, player);
                    ItemStackUtil.damage(tool, 1, serverPlayer);
                }
                PROCESSING.remove(targetPos);
            }
        });
    }

    private static boolean isLog(BlockState state) {
        return state.isIn(BlockTags.LOGS);
    }

    private static Set<BlockPos> findConnectedLogs(World world, BlockPos origin, int maxBlocks) {
        Set<BlockPos> found = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        for (BlockPos neighbor : getNeighbors(origin)) {
            queue.add(neighbor);
        }

        while (!queue.isEmpty() && found.size() < maxBlocks) {
            BlockPos current = queue.poll();
            if (found.contains(current)) continue;

            BlockState state = world.getBlockState(current);
            if (isLog(state)) {
                found.add(current);
                for (BlockPos neighbor : getNeighbors(current)) {
                    if (!found.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        return found;
    }

    private static BlockPos[] getNeighbors(BlockPos pos) {
        return new BlockPos[]{
                pos.up(), pos.down(),
                pos.north(), pos.south(),
                pos.east(), pos.west(),
                pos.up().north(), pos.up().south(),
                pos.up().east(), pos.up().west()
        };
    }
}
