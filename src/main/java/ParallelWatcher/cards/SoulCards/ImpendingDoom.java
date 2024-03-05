package ParallelWatcher.cards.SoulCards;

import ParallelWatcher.Actions.WitherAction;
import ParallelWatcher.cards.BaseSpiritorCard;
import ParallelWatcher.character.ParallelWatcher;
import ParallelWatcher.util.CardStats;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ImpendingDoom extends BaseSpiritorCard {

    public static final String ID = makeID("ImpendingDoom"); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    public static final CardStats info = new CardStats(ParallelWatcher.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public ImpendingDoom() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new WitherAction());
        if (upgraded) {
            addToBot(new WitherAction());
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }

    @Override
    public void superFlash(Color c) {
        super.superFlash(Color.BLACK);
    }
}
