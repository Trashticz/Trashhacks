package me.vaxry.harakiri.framework.event.world;

import me.vaxry.harakiri.framework.event.EventCancellable;

/**
 * Author Seth
 * 8/11/2019 @ 2:10 AM.
 */
public class EventGrassColor extends EventCancellable {

    private int color;

    public EventGrassColor() {
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
