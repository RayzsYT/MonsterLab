package de.rayzs.monsterlab.api.listener;

public interface MonsterEvent {
    MonsterEventType type();
    void execute(MonsterEventParameter parameter);
}
