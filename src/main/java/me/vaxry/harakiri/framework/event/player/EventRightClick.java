package me.vaxry.harakiri.framework.event.player;

import me.vaxry.harakiri.framework.event.EventCancellable;
import net.minecraft.util.EnumHand;

/**
 * Author Seth
 * 4/7/2019 @ 4:51 PM.
 */
public class EventRightClick extends EventCancellable {

    private EnumHand hand;

    public EventRightClick(EnumHand hand) {
        this.hand = hand;
    }

    public EnumHand getHand() {
        return hand;
    }

    public void setHand(EnumHand hand) {
        this.hand = hand;
    }
}
