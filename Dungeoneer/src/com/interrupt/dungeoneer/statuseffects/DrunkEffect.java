package com.interrupt.dungeoneer.statuseffects;

import com.interrupt.dungeoneer.entities.Actor;
import com.interrupt.managers.StringManager;

public class DrunkEffect extends StatusEffect {

    private float drunkFactor = 0.025f;

    public DrunkEffect() {
        this(2700);
    }

    public DrunkEffect(int time) {
        this.name = StringManager.get("statuseffects.DrunkEffect.defaultNameText");
        this.timer = time;
        this.statusEffectType = StatusEffectType.DRUNK;
    }

    public DrunkEffect(int time, int drunkFactor) {
        this(time);
        this.drunkFactor = drunkFactor;
    }

    @Override
    public void doTick(Actor owner, float delta) {
        owner.drunkMod += delta * drunkFactor;
    }

    public float getDrunkFactor() {
        return drunkFactor;
    }

    public void setDrunkFactor(float drunkFactor) {
        this.drunkFactor = drunkFactor;
    }
}
