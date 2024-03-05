package ParallelWatcher.cards;

import ParallelWatcher.util.CardStats;
import ParallelWatcher.Actions.ElapseAction;
import ParallelWatcher.character.ParallelWatcher;
import ParallelWatcher.orbs.SpiritOrb;
import ParallelWatcher.util.MyArrayUtil;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ParallelWatcher.ParallelWatcherMod;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class SpiritShield extends BaseSpiritorCard {

    public static final String ID = makeID("SpiritShield"); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    public static final CardStats info = new CardStats(
            ParallelWatcher.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public static final UIStrings uiString;
    public static final String[] TEXT;

    public SpiritShield() {
        super(ID, info);
    }

    public static final int BLOCK = 7;
    public static final int UPG = 2;

    static {
        uiString = CardCrawlGame.languagePack.getUIString(makeID("ChooseSoulCard"));
        TEXT = uiString.TEXT;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG);
        }
    }

    //todo modify the logic here to make the cards in select scene has normal scale
    //todo modify the logic, if not upgraded choose a random card
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardGroup soulCards = ParallelWatcherMod.getSoulCards();
        if (!soulCards.isEmpty()) {
            for (AbstractCard card : soulCards.group) {
                card.hover();
            }
            this.addToBot(new SelectCardsAction(soulCards.group, 1, TEXT[0] + 1 + TEXT[1], cards -> {
                AbstractOrb orb = ParallelWatcherMod.getBelongedOrb(MyArrayUtil.safeGet(cards, 0));
                if (orb instanceof SpiritOrb) {
                    ((SpiritOrb) orb).onEndOfTurn(false);
                    this.addToTop(new ElapseAction(orb, false));
                }
            }));
        }
        this.addToBot(new GainBlockAction(p, this.block));
    }
}
