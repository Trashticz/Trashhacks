package me.vaxry.harakiri.framework.event.player;

import me.vaxry.harakiri.framework.event.EventCancellable;
import net.minecraft.util.math.BlockPos;

/**
 * Author Seth
 * 4/24/2019 @ 1:36 PM.
 */
public class EventHittingPosition extends EventCancellable {

    private BlockPos blockPos;

    public EventHittingPosition(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
