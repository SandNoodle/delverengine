package com.interrupt.dungeoneer.statuseffects;

import com.badlogic.gdx.math.Vector3;
import com.interrupt.dungeoneer.entities.Actor;
import com.interrupt.dungeoneer.entities.Particle;
import com.interrupt.dungeoneer.entities.Player;
import com.interrupt.dungeoneer.game.CachePools;
import com.interrupt.dungeoneer.game.Game;
import com.interrupt.managers.StringManager;

public class SlowEffect extends StatusEffect {


    // Particle properties
    public Particle effectParticle;
	public Vector3 effectOffset = new Vector3(0.0f, 0.0f, 0.55f);

	private float particleInterval = 60f;
	private float particleTimer = 0;
    private float particleLifetime = 200;

	private int particleTexture = 83;

	private float scale = 1f;
	private float startScale = 1f;
	private float endScale = 0.125f;

	private float spreadMod = 1f;
	private float baseSpreadMod = 1f;
	private float playerSpreadMod = 2.75f;

	private float zMod = 0f;
	private float baseZMod = 0f;
	private float playerZMod = -0.3f;

    private float upwardVelocity = 0.003125f;
    private float turbulenceAmount = 0.025f;
    private float turbulenceMoveModifier = 0.025f;

    private String slowShader = "magic-item-white";

	public SlowEffect() {
		this(0.5f, 500);
		shader = slowShader;
	}

	public SlowEffect(float speedMod, int time) {
		this.name = StringManager.get("statuseffects.SlowEffect.defaultNameText");
		this.speedMod = speedMod;
		this.timer = time;
		this.statusEffectType = StatusEffectType.SLOW;

		if (speedMod > 1) speedMod = 1;
	}

    @Override
    public void onStatusBegin(Actor owner) {
        calculateSpreadMod();
    }

    @Override
	public void doTick(Actor owner, float delta) {
		this.particleTimer += delta;

		if (this.particleTimer > this.particleInterval) {
			this.particleTimer = 0f;
			this.createSlowParticle(owner, this.scale);
		}
	}

	private void createSlowParticle(Actor owner, float scale) {
		if (!this.showParticleEffect) {
			return;
		}

		Particle p = CachePools.getParticle();
		p.tex = this.particleTexture;
		p.lifetime = this.particleLifetime;
		p.fullbrite = true;
		p.checkCollision = false;
		p.floating = true;
        p.scale = scale;
		p.startScale = this.startScale;
		p.endScale = this.endScale;

        setParticlePosition(p);
        setParticleTurbulence(p);
        setParticleVelocity(p);

        Game.GetLevel().SpawnNonCollidingEntity(p);
	}

    private void calculateSpreadMod() {
	    if(this.owner instanceof Player) {
	        spreadMod = playerSpreadMod;
	        zMod = playerZMod;
        } else {
	        spreadMod = baseSpreadMod;
	        zMod = baseZMod;
        }
    }

    private void setParticleVelocity(Particle p) {
        p.za = this.upwardVelocity;
    }

    private void setParticleTurbulence(Particle p) {
        p.turbulenceAmount = this.turbulenceAmount;
        p.turbulenceMoveModifier = this.turbulenceMoveModifier;
    }

    private void setParticlePosition(Particle p) {
        p.x = this.owner.x + (Game.rand.nextFloat() * this.scale - (this.scale * 0.5f)) * this.spreadMod;
        p.y = this.owner.y + (Game.rand.nextFloat() * this.scale - (this.scale * 0.5f)) * this.spreadMod;
        p.z = this.owner.z + this.zMod;
    }

    public float getParticleInterval() {
        return particleInterval;
    }

    public void setParticleInterval(float particleInterval) {
        this.particleInterval = particleInterval;
    }

    public float getParticleTimer() {
        return particleTimer;
    }

    public void setParticleTimer(float particleTimer) {
        this.particleTimer = particleTimer;
    }

    public float getParticleLifetime() {
        return particleLifetime;
    }

    public void setParticleLifetime(float particleLifetime) {
        this.particleLifetime = particleLifetime;
    }

    public int getParticleTexture() {
        return particleTexture;
    }

    public void setParticleTexture(int particleTexture) {
        this.particleTexture = particleTexture;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
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

    public float getSpreadMod() {
        return spreadMod;
    }

    public void setSpreadMod(float spreadMod) {
        this.spreadMod = spreadMod;
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

    public float getzMod() {
        return zMod;
    }

    public void setzMod(float zMod) {
        this.zMod = zMod;
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

    public float getUpwardVelocity() {
        return upwardVelocity;
    }

    public void setUpwardVelocity(float upwardVelocity) {
        this.upwardVelocity = upwardVelocity;
    }

    public float getTurbulenceAmount() {
        return turbulenceAmount;
    }

    public void setTurbulenceAmount(float turbulenceAmount) {
        this.turbulenceAmount = turbulenceAmount;
    }

    public float getTurbulenceMoveModifier() {
        return turbulenceMoveModifier;
    }

    public void setTurbulenceMoveModifier(float turbulenceMoveModifier) {
        this.turbulenceMoveModifier = turbulenceMoveModifier;
    }

    public String getSlowShader() {
        return slowShader;
    }

    public void setSlowShader(String slowShader) {
        this.slowShader = slowShader;
    }
}
