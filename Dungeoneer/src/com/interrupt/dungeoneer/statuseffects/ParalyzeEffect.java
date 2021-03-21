package com.interrupt.dungeoneer.statuseffects;

import com.badlogic.gdx.math.Vector3;
import com.interrupt.dungeoneer.entities.Actor;
import com.interrupt.dungeoneer.entities.Particle;
import com.interrupt.dungeoneer.entities.Player;
import com.interrupt.dungeoneer.game.CachePools;
import com.interrupt.dungeoneer.game.Game;
import com.interrupt.managers.StringManager;

public class ParalyzeEffect extends StatusEffect {

    // Paralyze effect properties
    public boolean wasOwnerFloating = false;

    // Particle properties
    public Particle effectParticle;
    public Vector3 effectOffset = new Vector3(0.0f, 0.0f, 0.55f);

    private int impactParticleCount;
    private int varParticleCount = 3;
    private int baseParticleCount = 2;

    private float particleLifetime = 45f;

    private int paralyzeTexture = 71;
    private int startEffectTexture = 72;
    private int stopEffectTexture= 79;

    private float animationSpeed = 20f;

    private float scale = 0.75f;
    private float startScale = 1f;
    private float endScale = 1f;

    private float rotationAmount = 4.5f;

    private float zBaseVelocity = 0.025f;
    private float zVarVelocity = 0.05f;

    public ParalyzeEffect() {
        this(500);
    }

    public ParalyzeEffect(int time) {
        this.name = StringManager.get("statuseffects.ParalyzeEffect.defaultNameText");
        this.speedMod = 0.0f;
        this.timer = time;
        this.statusEffectType = StatusEffectType.PARALYZE;

        this.effectParticle = CachePools.getParticle();
        this.effectParticle.floating = true;
        this.effectParticle.lifetime = time;
        this.effectParticle.fullbrite = true;
        this.effectParticle.startScale = 1.0f;
        this.effectParticle.endScale = 1.0f;
        this.effectParticle.checkCollision = false;
        this.effectParticle.playAnimation(startEffectTexture, stopEffectTexture, animationSpeed, true);
        Game.GetLevel().SpawnNonCollidingEntity(this.effectParticle);
    }

    @Override
    public void doTick(Actor owner, float delta) {
        setParticlePosition(owner, this.effectParticle);

        if(owner instanceof Player) {
            this.effectParticle.x += Game.camera.direction.x * 0.25f;
            this.effectParticle.y += Game.camera.direction.z * 0.25f;
        }
    }

    @Override
    public void onStatusBegin(Actor owner) {
        this.wasOwnerFloating = owner.floating;
        owner.floating = false;

        this.impactParticleCount = Game.rand.nextInt(this.varParticleCount) + this.baseParticleCount;

        Vector3 cameraRight = Game.camera.direction.crs(new Vector3(0,1,0)).nor();

        if (!this.showParticleEffect) {
            return;
        }

        Vector3 particleDirection = CachePools.getVector3();

        for (int i = 0; i < impactParticleCount; i++) {
            createParalyzeParticle(owner, cameraRight, particleDirection, i);
        }

        CachePools.freeVector3(particleDirection);
    }

    private void createParalyzeParticle(Actor owner, Vector3 cameraRight, Vector3 particleDirection, int i) {
        Particle p = CachePools.getParticle();

        p.tex = paralyzeTexture;
        p.lifetime = particleLifetime;
        p.scale = scale;
        p.startScale = startScale;
        p.endScale = endScale;
        p.rotateAmount = -rotationAmount;
        p.fullbrite = true;
        p.checkCollision = false;

        setParticlePosition(owner, p);
        setParticleDirection(cameraRight, particleDirection, i, p);
        setParticleVelocity(particleDirection, p);

        Game.GetLevel().SpawnNonCollidingEntity(p);
    }

    @Override
    public void forPlayer(Player player) {
        timer *= 0.5;
    }

    @Override
    public void onStatusEnd(Actor owner) {
        this.active = false;
        this.effectParticle.lifetime = 0;
        CachePools.freeParticle(this.effectParticle);

        owner.floating = this.wasOwnerFloating;
    }

    private void setParticlePosition(Actor owner, Particle p) {
        p.x = owner.x + this.effectOffset.x;
        p.y = owner.y + this.effectOffset.y;
        p.z = owner.z + this.effectOffset.z;
    }

    private void setParticleVelocity(Vector3 particleDirection, Particle p) {
        p.xa = particleDirection.x;
        p.ya = particleDirection.z;
        p.za = Game.rand.nextFloat() * zVarVelocity + zBaseVelocity;
    }

    private void setParticleDirection(Vector3 cameraRight, Vector3 particleDirection, int i, Particle p) {
        particleDirection.set(cameraRight);
        particleDirection.scl(Game.rand.nextFloat() * 0.0125f + 0.0125f);

        if (i % 2 == 0) {
            particleDirection.scl(-1.0f);
            p.rotateAmount = rotationAmount;
        }
    }

    public int getImpactParticleCount() {
        return impactParticleCount;
    }

    public void setImpactParticleCount(int impactParticleCount) {
        this.impactParticleCount = impactParticleCount;
    }

    public int getVarParticleCount() {
        return varParticleCount;
    }

    public void setVarParticleCount(int varParticleCount) {
        this.varParticleCount = varParticleCount;
    }

    public int getBaseParticleCount() {
        return baseParticleCount;
    }

    public void setBaseParticleCount(int baseParticleCount) {
        this.baseParticleCount = baseParticleCount;
    }

    public float getParticleLifetime() {
        return particleLifetime;
    }

    public void setParticleLifetime(float particleLifetime) {
        this.particleLifetime = particleLifetime;
    }

    public int getParalyzeTexture() {
        return paralyzeTexture;
    }

    public void setParalyzeTexture(int paralyzeTexture) {
        this.paralyzeTexture = paralyzeTexture;
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

    public float getRotationAmount() {
        return rotationAmount;
    }

    public void setRotationAmount(float rotationAmount) {
        this.rotationAmount = rotationAmount;
    }

    public float getzBaseVelocity() {
        return zBaseVelocity;
    }

    public void setzBaseVelocity(float zBaseVelocity) {
        this.zBaseVelocity = zBaseVelocity;
    }

    public float getzVarVelocity() {
        return zVarVelocity;
    }

    public void setzVarVelocity(float zVarVelocity) {
        this.zVarVelocity = zVarVelocity;
    }

    public int getStartEffectTexture() {
        return startEffectTexture;
    }

    public void setStartEffectTexture(int startEffectTexture) {
        this.startEffectTexture = startEffectTexture;
    }

    public int getStopEffectTexture() {
        return stopEffectTexture;
    }

    public void setStopEffectTexture(int stopEffectTexture) {
        this.stopEffectTexture = stopEffectTexture;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }
}
