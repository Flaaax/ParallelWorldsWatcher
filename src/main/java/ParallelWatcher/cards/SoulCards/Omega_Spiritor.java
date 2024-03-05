package ParallelWatcher.cards.SoulCards;

import ParallelWatcher.Actions.ExecuteFuncAction;
import ParallelWatcher.cards.BaseSpiritorCard;
import ParallelWatcher.orbs.SpiritOrb;
import ParallelWatcher.util.CardStats;
import ParallelWatcher.util.MyArrayUtil;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Omega_Spiritor extends BaseSpiritorCard {

    public static final String ID = makeID("Omega_Spiritor");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public Omega_Spiritor() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.belongedOrb != null) {
            if (!this.upgraded) {
                /*this.addToBot(new ExecuteFuncAction(() -> {
                    ArrayList<AbstractOrb> orbs = p.orbs;
                    int index = orbs.indexOf(this.belongedOrb);
                    omegaTriggerOrb(MyArrayUtil.safeGet(orbs, index - 1));
                    omegaTriggerOrb(MyArrayUtil.safeGet(orbs, index + 1));
                }));*/
                ArrayList<AbstractOrb> orbs = p.orbs;
                int index = orbs.indexOf(this.belongedOrb);
                omegaTriggerOrb(MyArrayUtil.safeGet(orbs, index - 1));
                omegaTriggerOrb(MyArrayUtil.safeGet(orbs, index + 1));
            } else {
                this.addToBot(new ExecuteFuncAction(() -> {
                    Iterator<AbstractOrb> var3 = p.orbs.iterator();
                    while (var3.hasNext()) {
                        AbstractOrb orb = var3.next();
                        omegaTriggerOrb(orb);
                    }
                }));
            }
        }
    }

    void omegaTriggerOrb(AbstractOrb orb) {
        if (orb != null) {
            if (orb instanceof SpiritOrb) {
                if (!Objects.equals((((SpiritOrb) orb).card).cardID, ID)) {
                    orb.onEndOfTurn();
                }
            } else
                orb.onEndOfTurn();
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
        super.superFlash(Color.GOLD);
    }
}