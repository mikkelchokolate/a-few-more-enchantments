package net.mikkelchokolate.fewmoreenchantments.handler;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
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

public class VeinMiningHandler {

    private static final Set<BlockPos> PROCESSING = new HashSet<>();

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient()) return;
            if (!(player instanceof ServerPlayerEntity)) return;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            ItemStack tool = player.getMainHandStack();
            int level = ModEnchantments.getLevel(ModEnchantments.VEIN_MINING_ID, tool, world);
            if (level <= 0) return;

            if (!isOre(state)) return;
            if (PROCESSING.contains(pos)) return;

            int maxBlocks = 3 + (level * 3);
            Set<BlockPos> toBreak = findConnectedOres(world, pos, state.getBlock(), maxBlocks);

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

    private static boolean isOre(BlockState state) {
        return state.isIn(BlockTags.COAL_ORES) ||
                state.isIn(BlockTags.IRON_ORES) ||
                state.isIn(BlockTags.GOLD_ORES) ||
                state.isIn(BlockTags.DIAMOND_ORES) ||
                state.isIn(BlockTags.EMERALD_ORES) ||
                state.isIn(BlockTags.LAPIS_ORES) ||
                state.isIn(BlockTags.REDSTONE_ORES) ||
                state.isIn(BlockTags.COPPER_ORES);
    }

    private static Set<BlockPos> findConnectedOres(World world, BlockPos origin, Block oreBlock, int maxBlocks) {
        Set<BlockPos> found = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        for (BlockPos neighbor : getNeighbors(origin)) {
            queue.add(neighbor);
        }

        while (!queue.isEmpty() && found.size() < maxBlocks) {
            BlockPos current = queue.poll();
            if (found.contains(current)) continue;

            BlockState state = world.getBlockState(current);
            if (state.getBlock() == oreBlock) {
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
                pos.east(), pos.west()
        };
    }
}
