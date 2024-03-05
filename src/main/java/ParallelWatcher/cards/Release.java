package ParallelWatcher.cards;

import ParallelWatcher.Actions.ExecuteFuncAction;
import ParallelWatcher.character.ParallelWatcher;
import ParallelWatcher.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import java.util.ArrayList;

public class Release extends BaseSpiritorCard {

    public static final String ID = makeID("Release"); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"

    public static final CardStats info = new CardStats(ParallelWatcher.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public static final int DRAW = 2;
    public static final int UPG = 1;

    public Release() {
        super(ID, info);
        setMagic(DRAW);
    }

    //todo change its function

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(p, this.magicNumber));
        this.addToBot(new ExecuteFuncAction(() -> {
            ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;
            if (!orbs.isEmpty()) {
                AbstractOrb orb = orbs.get(0);
                AbstractDungeon.player.removeNextOrb();
                /*if(orb instanceof SpiritOrb){
                    ((SpiritOrb)orb).onEndOfTurn(false);
                    ((SpiritOrb)orb).onEndOfTurn(false);
                }
                else{
                    orb.onEndOfTurn();
                    orb.onEndOfTurn();
                }*/
                orb.onEndOfTurn();
                orb.onEndOfTurn();
            }
        }));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
