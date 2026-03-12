package net.mikkelchokolate.fewmoreenchantments.handler;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.mikkelchokolate.fewmoreenchantments.EnchantmentUtil;
import net.mikkelchokolate.fewmoreenchantments.ModEnchantments;

import java.util.List;

public class HarvestingHandler {

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient()) return ActionResult.PASS;
            if (!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;

            ItemStack tool = player.getStackInHand(hand);
            int level = EnchantmentUtil.getLevel(ModEnchantments.HARVESTING, tool, world.getRegistryManager());
            if (level <= 0) return ActionResult.PASS;

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (state.getBlock() instanceof CropBlock crop) {
                if (crop.isMature(state)) {
                    ServerWorld serverWorld = (ServerWorld) world;

                    List<ItemStack> drops = Block.getDroppedStacks(state, serverWorld, pos, null, player, tool);
                    for (ItemStack drop : drops) {
                        Block.dropStack(world, pos, drop);
                    }

                    world.setBlockState(pos, crop.getDefaultState());

                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        });
    }
}
