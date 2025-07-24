package net.tinhvv.listeners;


import org.bukkit.event.Event;

public interface EventRouter<T extends Event> {
    boolean accept(T event);
    void handle(T event);
}
