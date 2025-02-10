package net.diground.exyliaClasses.models.classes;

import net.diground.exyliaClasses.models.EClass;

import java.util.List;

public class Miner extends EClass {
    public Miner(String name, String displayName, List<String> passiveEffects, double warmupTime, List<String> activeEffects) {
        super(name, displayName, passiveEffects, warmupTime, activeEffects);
    }
}
