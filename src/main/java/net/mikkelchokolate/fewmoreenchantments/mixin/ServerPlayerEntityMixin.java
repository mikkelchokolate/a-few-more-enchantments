package net.mikkelchokolate.fewmoreenchantments.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.mikkelchokolate.fewmoreenchantments.ModEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Unique
    private static final Map<UUID, Map<Integer, ItemStack>> SOULBOUND_STASH = new HashMap<>();

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void saveSoulboundItems(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        Map<Integer, ItemStack> saved = new HashMap<>();

        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isEmpty() && ModEnchantments.hasEnchantment(
                    ModEnchantments.SOULBOUND_ID, stack, player.getEntityWorld())) {
                saved.put(i, stack.copy());
                player.getInventory().setStack(i, ItemStack.EMPTY);
            }
        }

        if (!saved.isEmpty()) {
            SOULBOUND_STASH.put(player.getUuid(), saved);
        }
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void restoreSoulboundItems(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if (alive) return;
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        UUID uuid = player.getUuid();

        Map<Integer, ItemStack> saved = SOULBOUND_STASH.remove(uuid);
        if (saved != null) {
            for (Map.Entry<Integer, ItemStack> entry : saved.entrySet()) {
                player.getInventory().setStack(entry.getKey(), entry.getValue());
            }
        }
    }
}
