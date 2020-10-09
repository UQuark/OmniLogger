package com.github.uquark0.omnilogger.mixin;

import com.github.uquark0.omnilogger.Server;
import com.github.uquark0.omnilogger.log.ActionInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.LocalTime;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
    @Shadow
    private final Block block;
    public BlockItemMixin(Settings settings, Block block) {
        super(settings);
        this.block = block;
    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"))
    public void place(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> ci) {
        if (!Thread.currentThread().getName().equals("Server thread"))
            return;

        BlockPos blockPos = context.getBlockPos();

        ActionInfo actionInfo = new ActionInfo(
                context.getPlayer(),
                ActionInfo.ActionType.BlockSet,
                blockPos,
                LocalTime.now(),
                block,
                context.getWorld().getRegistryKey()
        );
        Server.OMNI_LOGGER.log(actionInfo);
    }
}
