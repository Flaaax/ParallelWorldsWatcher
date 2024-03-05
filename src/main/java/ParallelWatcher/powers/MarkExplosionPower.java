package ParallelWatcher.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;

import static ParallelWatcher.ParallelWatcherMod.makeID;

public class MarkExplosionPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("MarkExplosion");
    private static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    //The only thing this controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public MarkExplosionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    //update marks, because you probably cant get mark amount when the owner dies
    /*@Override
    public void update(int slot) {
        super.update(slot);
        *//*if (!(this.owner.getPower(PoisonPower.POWER_ID) == null))
            this.ownerMarks = this.owner.getPower(PoisonPower.POWER_ID).amount;*//*
    }*/

    @Override
    public void onDeath() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            int ownerMarks = this.owner.getPower(MarkPower.POWER_ID).amount;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDead && !m.isDying) {
                    addToBot(new ApplyPowerAction(m, this.source, new MarkPower(m, ownerMarks * amount), ownerMarks * amount));
                }
            }
        }
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else this.description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    //Optional, for CloneablePowerInterface.
    @Override
    public AbstractPower makeCopy() {
        return new MarkExplosionPower(owner, amount);
    }
}
