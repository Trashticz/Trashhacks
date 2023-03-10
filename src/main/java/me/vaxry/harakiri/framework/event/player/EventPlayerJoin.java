package me.vaxry.harakiri.framework.event.player;

/**
 * Author Seth
 * 7/23/2019 @ 7:41 AM.
 */
public class EventPlayerJoin {

    private String name;
    private String uuid;

    public EventPlayerJoin(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
