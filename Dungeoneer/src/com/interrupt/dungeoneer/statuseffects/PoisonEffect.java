package com.interrupt.dungeoneer.statuseffects;

import com.badlogic.gdx.math.Vector3;
import com.interrupt.dungeoneer.Audio;
import com.interrupt.dungeoneer.entities.Actor;
import com.interrupt.dungeoneer.entities.Particle;
import com.interrupt.dungeoneer.entities.Player;
import com.interrupt.dungeoneer.entities.items.Weapon.DamageType;
import com.interrupt.dungeoneer.game.CachePools;
import com.interrupt.dungeoneer.game.Game;
import com.interrupt.managers.StringManager;

public class PoisonEffect extends StatusEffect {
    // Poison effect properties
    public float damageTimer = 60;
    private float dtimer = 0;

    public int damage = 1;
    private boolean canKill = false;

	// Particle properties
    public Particle effectParticle;
    public Vector3 effectOffset = new Vector3(0.0f, 0.0f, 0.55f);

    private float spreadMod = 1f;
    private float baseSpreadMod = 1f;
    private float playerSpreadMod = 2.75f;

    private float zMod = 0f;
    private float baseZMod = 0f;
    private float playerZMod = -0.3f;

    private float particleInterval = 20f;
    private float particleTimer = 0;

    private int particleTexture = 80;
    private int particleTextureVariations = 4;

    private float scaleRandomness = 0.25f;
    private float startScale = 1f;
    private float endScale = 0.125f;

    private float particleLifetime = 90f;

    private float upwardVelocity = 0.004f;

    // Audio properties
    private String poisonSound = "mg_pass_poison.mp3";
    private float soundVolume = 0.5f;
    private float soundRange = 6f;

	public PoisonEffect() {
		this(1000, 160, 1, false);
	}

	public PoisonEffect(int time, int damageTimer, int damage, boolean canKill) {
		this.name = StringManager.get("statuseffects.PoisonEffect.defaultNameText");
		this.timer = time;
		this.statusEffectType = StatusEffectType.POISON;
		this.damageTimer = damageTimer;
		this.damage = damage;
		this.canKill = canKill;
	}

	@Override
	public void doTick(Actor owner, float delta) {
		dtimer += delta;
		this.particleTimer += delta;

		if (this.particleTimer > this.particleInterval) {
			this.particleTimer = 0f;
			this.createPoisonParticle(owner, Game.rand.nextFloat() * scaleRandomness + scaleRandomness);
		}

		if(dtimer > damageTimer) {
			dtimer = 0;
			if (owner.hp - damage <= 0 && !canKill ) {
			    return;
			}

			owner.takeDamage(damage, DamageType.POISON, null);
			this.doPoisonEffect(owner);

			Audio.playPositionedSound(poisonSound, new Vector3(owner.x,owner.y,owner.z), soundVolume, soundRange);
		}
	}

	@Override
	public void onStatusBegin(Actor owner) {
		this.doPoisonEffect(owner);
	}

	private void doPoisonEffect(Actor owner) {
		int impactParticleCount = Game.rand.nextInt(8) + 2;
		for (int i = 0; i < impactParticleCount; i++) {
			this.createPoisonParticle(owner, 0.5f);
		}
	}

	private void createPoisonParticle(Actor owner, float scale) {
		if (!this.showParticleEffect) {
			return;
		}

		Particle p = CachePools.getParticle();
		p.tex = particleTexture + Game.rand.nextInt(particleTextureVariations -1);
		p.lifetime = particleLifetime;
		p.scale = scale;
		p.startScale = startScale;
		p.endScale = endScale;
		p.fullbrite = true;
		p.checkCollision = false;
		p.floating = true;
		p.x = owner.x + (Game.rand.nextFloat() * scale - (scale * 0.5f)) * spreadMod;
		p.y = owner.y + (Game.rand.nextFloat() * scale - (scale * 0.5f)) * spreadMod;
		p.z = owner.z + zMod;

		p.za = Game.rand.nextFloat() * upwardVelocity + upwardVelocity;

		Game.GetLevel().SpawnNonCollidingEntity(p);
	}

	@Override
	public void forPlayer(Player player) {
		player.history.poisoned();
	}

	@Override
	public void onStatusEnd(Actor owner) {
		this.active = false;
	}

    /** Calculates the Spread Modifier based on if the Entity is a Player instance */
    private void calculateSpreadMod() {
        if(owner instanceof Player) {
            this.spreadMod = playerSpreadMod;
            this.zMod = playerZMod;
        } else {
            this.spreadMod = baseSpreadMod;
            this.zMod = baseZMod;
        }
    }

    public float getDamageTimer() {
        return damageTimer;
    }

    public void setDamageTimer(float damageTimer) {
        this.damageTimer = damageTimer;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isCanKill() {
        return canKill;
    }

    public void setCanKill(boolean canKill) {
        this.canKill = canKill;
    }

    public float getBaseSpreadMod() {
        return baseSpreadMod;
    }

    public void setBaseSpreadMod(float baseSpreadMod) {
        this.baseSpreadMod = baseSpreadMod;
    }

    public float getPlayerSpreadMod() {
        return playerSpreadMod;
    }

    public void setPlayerSpreadMod(float playerSpreadMod) {
        this.playerSpreadMod = playerSpreadMod;
    }

    public float getBaseZMod() {
        return baseZMod;
    }

    public void setBaseZMod(float baseZMod) {
        this.baseZMod = baseZMod;
    }

    public float getPlayerZMod() {
        return playerZMod;
    }

    public void setPlayerZMod(float playerZMod) {
        this.playerZMod = playerZMod;
    }

    public float getParticleInterval() {
        return particleInterval;
    }

    public void setParticleInterval(float particleInterval) {
        this.particleInterval = particleInterval;
    }

    public int getParticleTexture() {
        return particleTexture;
    }

    public void setParticleTexture(int particleTexture) {
        this.particleTexture = particleTexture;
    }

    public int getParticleTextureVariations() {
        return particleTextureVariations;
    }

    public void setParticleTextureVariations(int particleTextureVariations) {
        this.particleTextureVariations = particleTextureVariations;
    }

    public float getStartScale() {
        return startScale;
    }

    public void setStartScale(float startScale) {
        this.startScale = startScale;
    }

    public float getEndScale() {
        return endScale;
    }

    public void setEndScale(float endScale) {
        this.endScale = endScale;
    }

    public float getParticleLifetime() {
        return particleLifetime;
    }

    public void setParticleLifetime(float particleLifetime) {
        this.particleLifetime = particleLifetime;
    }

    public float getUpwardVelocity() {
        return upwardVelocity;
    }

    public void setUpwardVelocity(float upwardVelocity) {
        this.upwardVelocity = upwardVelocity;
    }

    public String getPoisonSound() {
        return poisonSound;
    }

    public void setPoisonSound(String poisonSound) {
        this.poisonSound = poisonSound;
    }

    public float getAudioVolume() {
        return soundVolume;
    }

    public void setAudioVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

    public float getAudioRange() {
        return soundRange;
    }

    public void setAudioRange(float audioRange) {
        this.soundRange = audioRange;
    }

    public float getScaleRandomness() {
        return scaleRandomness;
    }

    public void setScaleRandomness(float scaleRandomness) {
        this.scaleRandomness = scaleRandomness;
    }
}
