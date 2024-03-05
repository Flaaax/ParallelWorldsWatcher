package ParallelWatcher.Actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PlayCardEffectAction extends AbstractGameAction {

    private final AbstractCard card;
    private final AbstractMonster target;
    private final AbstractPlayer source;

    private final boolean hasEffect;

    public PlayCardEffectAction(AbstractCard card, AbstractPlayer source, AbstractMonster target, boolean hasEffect) {
        this.card = card;
        this.target = target;
        this.source = source;
        this.hasEffect = hasEffect;
    }

    public PlayCardEffectAction(AbstractCard card, AbstractPlayer source, AbstractMonster target) {
        this(card, source, target, true);
    }

    public PlayCardEffectAction(AbstractCard card) {
        this(card, AbstractDungeon.player, AbstractDungeon.getRandomMonster());
    }

    public PlayCardEffectAction(AbstractCard card, boolean hasEffect) {
        this(card, AbstractDungeon.player, AbstractDungeon.getRandomMonster(), hasEffect);
    }

    @Override
    public void update() {
        //card.lighten(true);
        if (hasEffect) {
            this.addToBot(new WaitAction(1.2F));
            this.addToBot(new ExecuteFuncAction(() -> card.superFlash(Color.LIGHT_GRAY)));
        } else {
            this.addToBot(new WaitAction(0.4F));
        }
        card.use(source, target);
        this.isDone = true;
    }
}
