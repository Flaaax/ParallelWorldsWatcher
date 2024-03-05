package ParallelWatcher.cards.SoulCards;

import ParallelWatcher.character.ParallelWatcher;
import ParallelWatcher.util.CardStats;
import ParallelWatcher.cards.BaseSpiritorCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TemporalStrike extends BaseSpiritorCard {

    public static final String ID = makeID("TimeStrike"); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    public static final int DMG = 13;
    public static final int UPG_DMG = 5;

    private static final CardStats info = new CardStats(ParallelWatcher.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public TemporalStrike() {
        super(ID, info);
        tags.add(CardTags.STRIKE);
        setDamage(DMG);
        setElapseTime(2);
        thisChannels();
        thisReturns();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        //AbstractCard orbCard=this.makeStatEquivalentCopy();
        //orbCard.
        /*BaseSpiritorCard copy = (BaseSpiritorCard) this.makeStatEquivalentCopy();
        copy.setElapseTime(2);
        this.addToBot(new PlaceActualCardIntoOrb(copy));*/
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DMG);
        }
    }

    @Override
    public void triggerOnExhaust() {
        //is this necessary?
        //this.addToBot(new ExecuteFuncAction(() -> AbstractDungeon.player.exhaustPile.removeCard(this)));
    }

    @Override
    public void onElapse(AbstractMonster m) {
        this.addToBot(new DamageAction(
                m,
                new DamageInfo(AbstractDungeon.player, this.damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new TemporalStrike();
    }
}
