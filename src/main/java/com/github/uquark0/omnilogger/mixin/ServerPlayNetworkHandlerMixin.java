package com.github.uquark0.omnilogger.mixin;

import com.github.uquark0.omnilogger.Main;
import com.github.uquark0.omnilogger.log.ActionInfo;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
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
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onPlayerAction", at = @At("HEAD"))
    public void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        BlockPos blockPos = packet.getPos();
        PlayerActionC2SPacket.Action action = packet.getAction();

        if (action == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
            ActionInfo actionInfo = new ActionInfo(
                    player,
                    ActionInfo.ActionType.BlockDestroyed,
                    blockPos,
                    LocalTime.now(),
                    player.getServerWorld().getBlockState(blockPos).getBlock(),
                    player.getServerWorld().getRegistryKey()
            );
            Main.OMNI_LOGGER.log(actionInfo);
        }
    }
}
