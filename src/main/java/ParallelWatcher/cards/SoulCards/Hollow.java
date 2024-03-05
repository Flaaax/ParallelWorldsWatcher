package ParallelWatcher.cards.SoulCards;

import ParallelWatcher.powers.LoseEnergyOncePower;
import ParallelWatcher.cards.BaseSpiritorCard;
import ParallelWatcher.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Hollow extends BaseSpiritorCard {

    public static final String ID = makeID("Hollow");
    private static final CardStats info = new CardStats(
            CardColor.CURSE, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.CURSE, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            -2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public Hollow() {
        super(ID, info);
        setElapseTime(1);
        setMagic(1);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                new LoseEnergyOncePower(AbstractDungeon.player, this.magicNumber)
        ));
    }
}
