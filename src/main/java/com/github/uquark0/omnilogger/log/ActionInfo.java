package com.github.uquark0.omnilogger.log;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.time.LocalTime;

public class ActionInfo {
    public enum ActionType {
        BlockSet(1, "set"),
        BlockDestroyed(2, "destroyed");

        public final int id;
        public final String name;

        ActionType(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public final PlayerEntity player;
    public final ActionType action;
    public final BlockPos pos;
    public final LocalTime time;
    public final Block block;
    public final RegistryKey<World> world;

    public ActionInfo(PlayerEntity player, ActionType action, BlockPos pos, LocalTime time, Block block, RegistryKey<World> world) {
        this.player = player;
        this.action = action;
        this.pos = pos;
        this.time = time;
        this.block = block;
        this.world = world;
    }
}
