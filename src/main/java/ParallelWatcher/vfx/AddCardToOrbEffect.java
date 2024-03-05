package ParallelWatcher.vfx;

import ParallelWatcher.orbs.SpiritOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;


public class AddCardToOrbEffect
        extends AbstractGameEffect {
    private static final float EFFECT_DUR = 1.5F;
    private static final float PADDING = 30.0F * Settings.scale;

    private final AbstractCard card;

    private SpiritOrb o;
    private final float glowPoint;
    private boolean glowStartHit;
    private boolean firstApply = true;

    public AddCardToOrbEffect(AbstractCard srcCard, SpiritOrb o, float startX, float startY) {
        this.card = srcCard;
        this.duration = this.startingDuration = 0.1F;
        this.glowPoint = this.startingDuration * 0.25F;
        this.card.target_x = startX;
        this.card.target_y = startY;
        this.card.targetDrawScale = SpiritOrb.CARD_SCALE;
        this.card.targetAngle = 0.0F;
        this.o = o;
    }

    @Override
    public void update() {

        if (firstApply && this.o.source == null) {
            this.card.transparency = 0.0F;
            firstApply = false;
        }

        if (firstApply && (this.o.source == AbstractDungeon.player.limbo)) {
            this.o.source.removeCard(this.card);
            firstApply = false;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.card.update();

        if (this.duration < this.glowPoint) {
            if (!this.glowStartHit) {
                this.card.beginGlowing();
                //this.card.tags.add(GuardianMod.STASISGLOW);
                this.card.superFlash(Color.GOLDENROD);
                this.glowStartHit = true;
            }

            if (this.duration < 0.0F) {
                this.isDone = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDone)
            this.card.render(sb);
    }

    @Override
    public void dispose() {
    }
}


/* Location:              C:\Users\jacky\Desktop\downfall.jar!\guardian\vfx\AddCardToOrbEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */