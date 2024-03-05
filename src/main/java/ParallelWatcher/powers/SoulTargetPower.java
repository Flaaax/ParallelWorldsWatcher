package ParallelWatcher.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

import static ParallelWatcher.ParallelWatcherMod.makeID;

public class SoulTargetPower extends BasePower {

    public static final String POWER_ID = makeID("SoulTarget");
    private static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public SoulTargetPower(AbstractCreature owner) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);
        this.description = this.DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication() {
        MonsterGroup monsterGroup = AbstractDungeon.getMonsters();
        Iterator<AbstractMonster> var5 = monsterGroup.monsters.iterator();

        while (var5.hasNext()) {
            AbstractMonster m = var5.next();
            if (!m.isDeadOrEscaped() && m != this.owner && m.hasPower(this.ID)) {
                addToTop(new RemoveSpecificPowerAction(m, AbstractDungeon.player, this.ID));
            }
        }
    }

    //does not do anything here, just a symbol power
}
