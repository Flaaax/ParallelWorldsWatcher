package ParallelWatcher.Actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayCertainCardAction extends AbstractGameAction {

    private static final Logger logger = LogManager.getLogger(PlayCertainCardAction.class);
    public final AbstractCard card;

    //IDK what if monster is null?
    /*public PlayCertainCardAction(AbstractCard card) {
        this(card, AbstractDungeon.getRandomMonster());
    }

    public PlayCertainCardAction(AbstractCard card, AbstractCreature target) {
        this(card, target, true);
    }

    public PlayCertainCardAction(AbstractCard card, AbstractCreature target, boolean isCopy) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.target = target;
        if (!isCopy) {
            this.card = card;
        } else this.card = card.makeCopy();
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            //AbstractDungeon.getCurrRoom().souls.remove(card);
            card.exhaustOnUseOnce = true;
            AbstractDungeon.player.limbo.group.add(card);
            //card.current_y = -200.0F * Settings.scale;
            card.current_x = Settings.WIDTH / 2.0F;
            card.current_y = Settings.HEIGHT / 2.0F;
            card.target_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
            card.target_y = (float) Settings.HEIGHT / 2.0F;
            card.targetAngle = 0.0F;
            card.lighten(false);
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.applyPowers();
            this.addToTop(new NewQueueCardAction(card, this.target, false, true));
            this.addToTop(new UnlimboAction(card));
            if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            } else {
                this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            }

            this.isDone = true;
        }
    }*/

    PlayCertainCardAction(AbstractCard card) {
        this.duration = 0.5F;
        this.card = card;

    }

    @Override
    public void update() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
        } else {
            if (this.duration == 0.5F) {
                GameActionManager.queueExtraCard(this.card, AbstractDungeon.getRandomMonster());
                logger.info("Played " + this.card.name);
            }

            this.tickDuration();
        }

    }
}
