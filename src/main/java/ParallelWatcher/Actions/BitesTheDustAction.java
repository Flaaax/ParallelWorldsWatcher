package ParallelWatcher.Actions;

import ParallelWatcher.cards.BitesTheDust;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;

public class BitesTheDustAction extends AbstractGameAction {

    private final AbstractPlayer player;

    private final boolean upgraded;

    public BitesTheDustAction(boolean upgraded) {
        player = AbstractDungeon.player;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        //this.addToBot(new RemoveAllPowersAction(player, false));
        this.addToTop(new DiscardAction(player, player, player.hand.size(), true));
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
        AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
        this.addToBot(new ExecuteFuncAction(() -> {
            player.hand.clear();
            player.exhaustPile.clear();
            player.discardPile.clear();
            player.drawPile.initializeDeck(player.masterDeck);
            this.removeCard();
            //addToBot(new DecreaseMaxOrbAction(player.orbs.size()));
            //addToBot(new IncreaseMaxOrbAction(3));
            AbstractDungeon.actionManager.callEndTurnEarlySequence();
        }));
        //this.addToBot(new RemoveAllOrbsAction());
        this.addToBot(new SkipEnemiesTurnAction());
        //this.addToBot(new RemoveAllTemporaryHPAction(player,player));
        //AbstractDungeon.actionManager.callEndTurnEarlySequence();

        //this.addToBot(new PressEndTurnButtonAction());
        this.isDone = true;
    }

    private void removeCard() {
        for (AbstractCard card : player.drawPile.group) {
            if (card instanceof BitesTheDust && (card.upgraded == this.upgraded)) {
                player.drawPile.group.remove(card);
                return;
            }
        }
    }
}
