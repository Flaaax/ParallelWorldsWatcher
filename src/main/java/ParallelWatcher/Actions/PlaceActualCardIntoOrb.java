package ParallelWatcher.Actions;

import ParallelWatcher.ParallelWatcherMod;
import ParallelWatcher.orbs.SpiritOrb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlaceActualCardIntoOrb extends AbstractGameAction {

    private final AbstractCard card;
    private final CardGroup source;
    private boolean skipWait;
    private final boolean hadRetain;
    //public static final String[] TEXT = (CardCrawlGame.languagePack.getUIString("Guardian:UIOptions")).TEXT;

    public boolean dontEvoke;

    public PlaceActualCardIntoOrb(AbstractCard card, CardGroup source) {
        this.card = card;
        this.source = source;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.skipWait = false;
        this.hadRetain = card.retain;
        if (source != null && source.type == CardGroup.CardGroupType.HAND) {
            card.retain = true;
        }
        this.dontEvoke = false;
    }

    public PlaceActualCardIntoOrb(AbstractCard card) {
        this(card, null);
    }

    public PlaceActualCardIntoOrb(AbstractCard card, boolean dontEvoke) {
        this(card, null);
        this.dontEvoke = dontEvoke;
    }

    public PlaceActualCardIntoOrb(AbstractCard card, CardGroup source, boolean skipWait) {
        this(card, source);
        this.skipWait = skipWait;
    }

    @Override
    public void update() {
        //if (true || ParallelWatcherMod.canSpawnSpiritOrb()) {
        if (!AbstractDungeon.player.hasEmptyOrb()) {
                /*for (AbstractOrb o : AbstractDungeon.player.orbs) {
                    if (!(o instanceof SpiritOrb)) {

                        AbstractDungeon.player.orbs.remove(o);
                        AbstractDungeon.player.orbs.add(0, o);
                        AbstractDungeon.player.evokeOrb();
                        break;
                    }
                }*/
            //this.addToTop(new EvokeOrbAction(1));
        }
        if (!this.skipWait && !Settings.FAST_MODE) addToTop(new WaitAction(0.1F));
        if (!this.dontEvoke || ParallelWatcherMod.canSpawnSpiritOrb()) {
            addToTop(new ChannelAction(new SpiritOrb(this.card, this.source)));
        }
        /*} else {
        this.card.retain = this.hadRetain;
        if (!AbstractDungeon.player.hasEmptyOrb()) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, "TEST_NO_EMPTY_ORB", true));
        }
        }*/

        this.isDone = true;
    }
}
