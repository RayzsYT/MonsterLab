package de.rayzs.monsterlab.api.monster.attack;

import de.rayzs.monsterlab.api.monster.Monster;

public interface Attack {
    long energyCost();
    void execute(Monster monster);
}
