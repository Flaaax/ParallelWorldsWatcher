package ParallelWatcher.Actions;

import ParallelWatcher.ParallelWatcherMod;
import ParallelWatcher.orbs.SpiritOrb;
import ParallelWatcher.cards.BaseSpiritorCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import java.util.ArrayList;
import java.util.Collections;

public class ElapseAction extends AbstractGameAction {

    public final AbstractOrb orb;
    public final boolean triggerElapse;

    //only affects instance of SpiritOrb
    public ElapseAction(AbstractOrb orb) {
        this(orb, true);
    }

    public ElapseAction(AbstractOrb orb, boolean triggerElapse) {
        this.orb = orb;
        this.triggerElapse = triggerElapse;
    }

    @Override
    public void update() {
        if (this.orb != null) {
            if (this.orb instanceof SpiritOrb) {
                SpiritOrb orb = (SpiritOrb) this.orb;

                AbstractCard card = orb.card;

                if (AbstractDungeon.player.hoveredCard == card) {
                    AbstractDungeon.player.releaseCard();
                }

                /*AbstractDungeon.actionManager.removeFromQueue(card);
                card.unhover();
                card.untip();
                card.stopGlowing();
                AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(orb.card));*/

                AbstractDungeon.player.limbo.addToTop(card);
                this.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));

                //orb.card.current_x = x;
                //orb.card.current_y = y;
                //orb.card.target_x = x;
                //orb.card.target_y = y;
                //card.targetDrawScale = 0.75F;
                //card.drawScale = 0.75F;
                //card.update();
                //orb.card.current_x=x;
                //orb.card.current_y=y;
                //new PlayTopCardAction()
                //this.addToTop(new UnlimboAction(orb.card));
                this.removeOrb(orb);
                if (card instanceof BaseSpiritorCard) {
                    this.addToTop(new ExecuteFuncAction(() -> {
                        ((BaseSpiritorCard) card).onElapse(ParallelWatcherMod.getOrbRandomMonster());
                        if (card.hasTag(ParallelWatcherMod.RETURN)) {
                            ((BaseSpiritorCard) card).resetElapse();
                            this.addToTop(new MakeTempCardInHandAction(card));
                        }
                    }));
                }
            }
        }
        this.isDone = true;
    }

    public void removeOrb(AbstractOrb orb) {
        ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;
        int index = orbs.indexOf(orb);
        if (index != -1) {

            orbs.remove(orb);
            orbs.add(0, orb);
            if (!(orbs.get(0) instanceof EmptyOrbSlot)) {
                //orbs.get(0).onEvoke();
                AbstractOrb orbSlot = new EmptyOrbSlot();

                for (int i = 1; i < orbs.size(); ++i) {
                    Collections.swap(orbs, i, i - 1);
                }

                orbs.set(orbs.size() - 1, orbSlot);

                for (int i = 0; i < orbs.size(); ++i) {
                    orbs.get(i).setSlot(i, AbstractDungeon.player.maxOrbs);
                }
            }

        }
    }

}
