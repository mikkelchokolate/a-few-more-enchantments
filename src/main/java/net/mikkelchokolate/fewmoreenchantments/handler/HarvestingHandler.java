package net.mikkelchokolate.fewmoreenchantments.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.mikkelchokolate.fewmoreenchantments.ModEnchantments;
import net.pitan76.mcpitanlib.api.event.result.EventResult;
import net.pitan76.mcpitanlib.api.event.v0.InteractionEventRegistry;

import java.util.List;

public class HarvestingHandler {

    public static void register() {
        InteractionEventRegistry.registerRightClickBlock(event -> {
            if (event.getPlayer().isClient()) return EventResult.pass();
            if (!event.getPlayer().isServerPlayer()) return EventResult.pass();

            ItemStack tool = event.getStackInHand();
            int level = ModEnchantments.getLevel(ModEnchantments.HARVESTING_ID, tool, event.getWorld());
            if (level <= 0) return EventResult.pass();

            BlockPos pos = event.getPos();
            BlockState state = event.getBlockState();

            if (state.getBlock() instanceof CropBlock) {
                CropBlock crop = (CropBlock) state.getBlock();
                if (crop.isMature(state)) {
                    ServerWorld serverWorld = (ServerWorld) event.getWorld();

                    List<ItemStack> drops = Block.getDroppedStacks(state, serverWorld, pos, null,
                            event.getPlayer().getPlayerEntity(), tool);
                    for (ItemStack drop : drops) {
                        Block.dropStack(serverWorld, pos, drop);
                    }

                    serverWorld.setBlockState(pos, crop.getDefaultState());

                    return EventResult.success();
                }
            }

            return EventResult.pass();
        });
    }
}
