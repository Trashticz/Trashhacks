package me.vaxry.harakiri.framework.event.network;

import me.vaxry.harakiri.framework.event.EventCancellable;
import net.minecraft.network.Packet;

/**
 * Author Seth
 * 4/6/2019 @ 1:42 PM.
 */
public final class EventSendPacket extends EventCancellable {

    private Packet packet;

    public EventSendPacket(EventStage stage, Packet packet) {
        super(stage);
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
