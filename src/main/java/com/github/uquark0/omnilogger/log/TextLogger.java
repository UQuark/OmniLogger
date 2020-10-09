package com.github.uquark0.omnilogger.log;

import net.minecraft.util.registry.Registry;

public abstract class TextLogger implements OmniLogger {
    private static final String TEMPLATE = "%s %s %s@[%d, %d, %d]@%s";

    @Override
    public void log(ActionInfo info) {
        print(String.format(
                TEMPLATE,
                info.player.getEntityName(),
                info.action.name,
                Registry.BLOCK.getId(info.block).toString(),
                info.pos.getX(),
                info.pos.getY(),
                info.pos.getZ(),
                info.world.getValue().toString()
        ));
    }

    public abstract void print(String text);
}
