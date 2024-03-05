package ParallelWatcher.cards;

import ParallelWatcher.Actions.PlaceActualCardIntoOrb;
import ParallelWatcher.cards.SoulCards.Omega_Spiritor;
import ParallelWatcher.util.CardStats;
import ParallelWatcher.character.ParallelWatcher;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AlchemistSpell extends BaseSpiritorCard {

    public static final String ID = makeID("AlchemistSpell");
    private static final CardStats info = new CardStats(
            ParallelWatcher.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public AlchemistSpell() {
        super(ID, info);
        this.cardsToPreview = new Omega_Spiritor();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        AbstractCard card = new Omega_Spiritor();
        if (upgraded) {
            card.upgrade();
        }
        addToBot(new PlaceActualCardIntoOrb(card));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.cardsToPreview.upgrade();
            upgradeDescription();
        }
    }
}
