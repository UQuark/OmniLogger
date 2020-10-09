package com.github.uquark0.omnilogger.mixin;

import com.github.uquark0.omnilogger.Server;
import com.github.uquark0.omnilogger.log.ActionInfo;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalTime;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;
    @Shadow
    public MinecraftServer server;

    private boolean isBlockDestroyed(PlayerActionC2SPacket.Action action, BlockPos blockPos) {
        float blockHardness = player.getServerWorld().getBlockState(blockPos).getHardness(player.getServerWorld(), blockPos);
        return
                action == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK ||
                (
                        action == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK &&
                        (player.isCreative() || blockHardness == 0)
                );
    }
    @Inject(method = "onPlayerAction", at = @At("HEAD"))
    public void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (!Thread.currentThread().getName().equals("Server thread"))
            return;

        BlockPos blockPos = packet.getPos();
        PlayerActionC2SPacket.Action action = packet.getAction();

        if (isBlockDestroyed(action, blockPos)) {
            ActionInfo actionInfo = new ActionInfo(
                    player,
                    ActionInfo.ActionType.BlockDestroyed,
                    blockPos,
                    LocalTime.now(),
                    player.getServerWorld().getBlockState(blockPos).getBlock(),
                    player.getServerWorld().getRegistryKey()
            );
            Server.OMNI_LOGGER.log(actionInfo);
        }
    }
}
