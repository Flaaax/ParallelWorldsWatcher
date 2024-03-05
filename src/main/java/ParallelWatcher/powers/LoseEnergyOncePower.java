package ParallelWatcher.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static ParallelWatcher.ParallelWatcherMod.makeID;

public class LoseEnergyOncePower extends BasePower {

    public static final String POWER_ID = makeID("LoseEnergyOnce");
    private static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    public LoseEnergyOncePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.DESCRIPTIONS[0]);

        for (int i = 0; i < this.amount; ++i) {
            sb.append("[E] ");
        }

        if(!this.DESCRIPTIONS[1].isEmpty()){
            sb.append(this.DESCRIPTIONS[1]);
        }

        this.description = sb.toString();
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new LoseEnergyAction(this.amount));
        this.flash();
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

}
