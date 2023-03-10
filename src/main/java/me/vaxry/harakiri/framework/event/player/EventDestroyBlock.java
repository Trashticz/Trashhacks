package me.vaxry.harakiri.framework.event.player;

import me.vaxry.harakiri.framework.event.EventCancellable;
import net.minecraft.util.math.BlockPos;

/**
 * Author Seth
 * 4/6/2019 @ 6:16 PM.
 */
public class EventDestroyBlock extends EventCancellable {

    private BlockPos pos;

    public EventDestroyBlock(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }
}
