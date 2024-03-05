package ParallelWatcher.orbs;

import ParallelWatcher.Actions.PlayCardEffectAction;
import ParallelWatcher.ParallelWatcherMod;
import ParallelWatcher.WatcherHelper;
import ParallelWatcher.vfx.AddCardToOrbEffect;
import ParallelWatcher.Actions.ElapseAction;
import ParallelWatcher.cards.BaseSpiritorCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

//basically the code of StasisOrb in downfall guardian, hope they don't mind
public class SpiritOrb extends AbstractOrb {
    private static final String ID = ParallelWatcherMod.makeID("SpiritOrb");

    public final AbstractCard card;

    private AbstractGameEffect stasisStartEffect;

    private static OrbStrings orbString = new OrbStrings();

    public final CardGroup source;

    public static final float CARD_SCALE = 0.2F;

    public static final float CARD_HOVER_SCALE = 0.8F;

    static {
        orbString = CardCrawlGame.languagePack.getOrbString(ID);
        // DESC = orbString.DESCRIPTION;
    }

    public SpiritOrb(AbstractCard card) {
        this(card, null);
    }

    public SpiritOrb(AbstractCard card, CardGroup source) {
        this.card = card;
        if (card instanceof BaseSpiritorCard) {
            ((BaseSpiritorCard) card).belongedOrb = this;
        }
        ParallelWatcherMod.logger.info("New Spirit Orb made.");
        this.card.beginGlowing();
        this.name = this.card.name + orbString.DESCRIPTION[1];
        this.channelAnimTimer = 0.5F;

        updateDescription();

        this.source = source;

        initialize();
    }

    private void initialize() {
        if (source != null) {

            if (source != AbstractDungeon.player.limbo) {
                source.removeCard(this.card);
            }

            switch (source.type) {
                case HAND:
                    this.stasisStartEffect = new AddCardToOrbEffect(this.card, this, this.card.current_x, this.card.current_y);
                    break;
                case DRAW_PILE:
                    this.stasisStartEffect = new AddCardToOrbEffect(this.card, this, AbstractDungeon.overlayMenu.combatDeckPanel.current_x + 100.0F * Settings.scale, AbstractDungeon.overlayMenu.combatDeckPanel.current_y + 100.0F * Settings.scale);
                    break;
                case DISCARD_PILE:
                    this.stasisStartEffect = new AddCardToOrbEffect(this.card, this, AbstractDungeon.overlayMenu.discardPilePanel.current_x - 100.0F * Settings.scale, AbstractDungeon.overlayMenu.discardPilePanel.current_y + 100.0F * Settings.scale);
                    break;
                case EXHAUST_PILE:
                    this.card.unfadeOut();
                    this.stasisStartEffect = new AddCardToOrbEffect(this.card, this, AbstractDungeon.overlayMenu.discardPilePanel.current_x - 100.0F * Settings.scale, AbstractDungeon.overlayMenu.exhaustPanel.current_y + 100.0F * Settings.scale);
                    break;
                default:
                    this.stasisStartEffect = new AddCardToOrbEffect(this.card, this, card.hb.x + card.hb.width / 2.0F, card.hb.y + card.hb.height / 2.0F);
                    break;
            }
        } else {
            //this.stasisStartEffect = new AddCardToOrbEffect(this.card, this, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, !selfStasis);
            AbstractOrb orb = WatcherHelper.getOrbChannelPos();
            if (orb != null) {
                Hitbox hb = WatcherHelper.getOrbChannelPos().hb;
                float cx = hb.x + hb.width / 2.0F;
                float cy = hb.y + hb.height / 2.0F;
                this.card.drawScale = CARD_SCALE;
                this.card.current_x = cx;
                this.card.current_y = cy;
                this.stasisStartEffect = new AddCardToOrbEffect(this.card, this, cx, cy);
            } else {
                this.stasisStartEffect = new AddCardToOrbEffect(this.card, this, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY);
            }
        }

        AbstractDungeon.effectsQueue.add(this.stasisStartEffect);
        this.card.retain = false;
    }

    @Override
    public void updateDescription() {
        this.description = orbString.DESCRIPTION[0];
    }

    @Override
    public void onStartOfTurn() {
       /* AbstractDungeon.actionManager.addToTop((AbstractGameAction) new WaitAction(1.4F));
        AbstractDungeon.actionManager.addToTop(new PlayCertainCardAction(card));

        updateDescription();*/
        if (this.card instanceof BaseSpiritorCard) {
            ((BaseSpiritorCard) card).onOrbStartOfTurn();
        }
    }

    public void onEndOfTurn(boolean elapse) {
        //this.addToTop(new NewQueueCardAction(card,  AbstractDungeon.getRandomMonster(), false, true));
        //this.addToTop(new UseCardAction(new Strike_Spiritor()));
        //this.addToTop(new UseCardAction(card.makeCopy(), AbstractDungeon.getRandomMonster()));
        if (this.card instanceof BaseSpiritorCard) {
            BaseSpiritorCard card = (BaseSpiritorCard) this.card;
            if (card.useWhenOrbActivated) {
                this.addToBot(new PlayCardEffectAction(card, AbstractDungeon.player, ParallelWatcherMod.getOrbRandomMonster()));
            }
            card.onOrbActivate();
            if (elapse) {
                card.doElapse(1);
                if (card.elapse && card.elapseTime <= 0) {
                /*AbstractDungeon.player.orbs.add(0, this);
                AbstractDungeon.player.evokeOrb();*/
                    this.addToBot(new ElapseAction(this));
                }
            }
        } else {
            this.addToBot(new PlayCardEffectAction(card, AbstractDungeon.player, ParallelWatcherMod.getOrbRandomMonster()));
        }
    }

    @Override
    public void onEndOfTurn() {
        onEndOfTurn(true);
    }

    public void onOmegaTrigger() {
        if (this.card instanceof BaseSpiritorCard) {
            BaseSpiritorCard card = (BaseSpiritorCard) this.card;
            if (card.useWhenOrbActivated) {
                this.addToBot(new PlayCardEffectAction(card, false));
            }
            card.onOrbActivate();
            card.doElapse(1);
            if (card.elapse && card.elapseTime == 0) {
                /*AbstractDungeon.player.orbs.add(0, this);
                AbstractDungeon.player.evokeOrb();*/
                this.addToBot(new ElapseAction(this));
            }
        } else {
            this.addToBot(new PlayCardEffectAction(card, false));
        }
    }

    private void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    private void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    @Override
    public void onEvoke() {
        if (this.card instanceof BaseSpiritorCard) {
            ((BaseSpiritorCard) card).onEvoke();
            if (card.hasTag(ParallelWatcherMod.RETURN)) {
                ((BaseSpiritorCard) card).resetElapse();
                this.addToBot(new MakeTempCardInHandAction(card));
            }
        }
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
    }

    @Override
    public AbstractOrb makeCopy() {
        return new SpiritOrb(card.makeCopy(), null);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (/*!this.hb.hovered && */(this.stasisStartEffect == null || this.stasisStartEffect.isDone)) {
            renderActual(spriteBatch);
        }
    }

    public void renderActual(SpriteBatch sb) {
        this.card.render(sb);
        if ((this.card instanceof BaseSpiritorCard)) {
            BaseSpiritorCard card = (BaseSpiritorCard) this.card;
            if (card.elapse && !this.hb.hovered) {
                this.evokeAmount = this.passiveAmount = card.elapseTime;
                renderText(sb);
            }
        }
        this.hb.render(sb);
    }

    @Override
    public void playChannelSFX() {
    }

    public void update() {
        super.update();
        if (this.stasisStartEffect == null || this.stasisStartEffect.isDone) {
            this.card.targetAngle = 0.0F;
            this.card.target_x = this.tX;
            this.card.target_y = this.tY;
            this.card.applyPowers();

            if (this.hb.hovered) {
                this.card.targetDrawScale = CARD_HOVER_SCALE;
            } else {
                this.card.targetDrawScale = CARD_SCALE;
            }
        }
        this.card.update();
    }
}
