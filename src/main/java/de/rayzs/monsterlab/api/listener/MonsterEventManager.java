package de.rayzs.monsterlab.api.listener;

import java.util.*;

public class MonsterEventManager {

    private static final List<MonsterEvent> EVENTS = new ArrayList<>();

    /**
     * Register a new event
     *
     * @param event The event which should be registered
     * @return Boolean if event could be registered or not
     */
    public static boolean register(final MonsterEvent event) {
        return EVENTS.add(event);
    }

    /**
     * Loop & call all events with the matching type with the following parameters
     *
     * @param type Type of what kind of events should be triggered
     * @param parameter Includes information like the monster & damager-entity (e.g: player)
     */
    public static void call(final MonsterEventType type, final MonsterEventParameter parameter) {
        EVENTS.stream().filter(event -> event.type() == type).forEach(event -> event.execute(parameter));
    }
}
