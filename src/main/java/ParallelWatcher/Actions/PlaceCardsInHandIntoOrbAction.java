package ParallelWatcher.Actions;

import ParallelWatcher.ParallelWatcherMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.ArrayList;

public class PlaceCardsInHandIntoOrbAction extends AbstractGameAction {
    private ArrayList<AbstractCard> invalidTargets = new ArrayList<>();
    private final boolean endOfTurn;
    private final boolean anyNumber;

    public PlaceCardsInHandIntoOrbAction(AbstractCreature source, int amount, boolean anyNumber) {
        this(source, amount, anyNumber, false);
    }

    public PlaceCardsInHandIntoOrbAction(AbstractCreature source, int amount, boolean anyNumber, boolean endOfTurn) {
        setValues((AbstractCreature) AbstractDungeon.player, source, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.anyNumber = anyNumber;
        this.endOfTurn = endOfTurn;
    }

    @Override
    public void update() {
        if (this.duration == 0.5F) {

            if (this.endOfTurn) {
                ArrayList<AbstractCard> toMove = new ArrayList<>();
                toMove.addAll(AbstractDungeon.player.hand.group);
                for (AbstractCard c : toMove) {
                    if (c.isEthereal) {
                        AbstractDungeon.player.hand.removeCard(c);
                        this.invalidTargets.add(c);
                    }
                }
            }

            if (this.amount > AbstractDungeon.player.hand.size()) this.amount = AbstractDungeon.player.hand.size();

            if (this.amount == 0) {
                this.isDone = true;

                return;
            }
            if (!ParallelWatcherMod.canSpawnSpiritOrb()) {
                this.isDone = true;
                if (!AbstractDungeon.player.hasEmptyOrb()) {
                    AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, "TEXT_NO_SPACES", true));
                }
                return;
            }
            AbstractDungeon.handCardSelectScreen.open("TEXT_CHOOSING_CARDS", this.amount, false, this.anyNumber, false, false, this.anyNumber);
        } else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            ArrayList<AbstractCard> cards = AbstractDungeon.handCardSelectScreen.selectedCards.group;


            addToTop(new AbstractGameAction()
                    /*    */ {
                /*    */
                public void update() {
                    this.isDone = true;
                    for (AbstractCard c : PlaceCardsInHandIntoOrbAction.this.invalidTargets)
                        AbstractDungeon.player.hand.addToTop(c);
                    AbstractDungeon.player.hand.refreshHandLayout();
                }
                /*    */
            });

            for (int i = cards.size() - 1; i >= 0; i--) {
                AbstractCard c = cards.get(i);
                //addToTop(new PlaceActualCardIntoStasis(c, AbstractDungeon.player.hand, true));
            }

            for (AbstractCard card : cards) {
                AbstractDungeon.player.hand.addToTop(card);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        tickDuration();
    }
}
