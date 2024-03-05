package ParallelWatcher.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import java.util.ArrayList;

import static ParallelWatcher.ParallelWatcherMod.makeID;

public class HysteriaPower extends BasePower {

    public static final String POWER_ID = makeID("Hysteria");
    private static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    public ArrayList<DamageInfo> damageSources = new ArrayList<>();

    public HysteriaPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }


    //todo change an icon

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
            this.flash();
            damageSources.add(new DamageInfo(info.owner, damageAmount, DamageInfo.DamageType.HP_LOSS));
        }
        return 0;
    }

    @Override
    public void atStartOfTurn() {
        if (!damageSources.isEmpty()) {
            addToBot(new VFXAction(this.owner, new ScreenOnFireEffect(), 0.01F));
            for (DamageInfo dmg : damageSources) {
                if (dmg.owner != null && dmg.owner != this.owner && !dmg.owner.isDeadOrEscaped()) {
                    addToBot(new WaitAction(1.2F));
                    addToBot(new DamageAction(
                            dmg.owner,
                            new DamageInfo(AbstractDungeon.player, dmg.base, DamageInfo.DamageType.THORNS),
                            AbstractGameAction.AttackEffect.FIRE
                    ));
                }
            }
        }
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    /*    public int trueDamage;

    public AbstractCreature newTarget;

    public HysteriaPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        if (owner instanceof AbstractMonster) {
            newTarget = AbstractDungeon.getRandomMonster((AbstractMonster) owner);
            if (newTarget == null) {
                newTarget = owner;
            }
        } else if (owner instanceof AbstractPlayer) {
            newTarget = owner;
        }
        if (newTarget == null) {
            System.out.println("what is going on???");
        }
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (owner instanceof AbstractMonster && target instanceof AbstractPlayer) {
            if (newTarget == null || newTarget.isDeadOrEscaped()) {
                newTarget = AbstractDungeon.getRandomMonster((AbstractMonster) owner);
                if (newTarget == null) {
                    newTarget = owner;
                }
            }
            if(trueDamage==0) System.out.println("holy sh?t");
            addToBot(new DamageAction(newTarget, new DamageInfo(owner, trueDamage, DamageInfo.DamageType.THORNS)));
        } else if (owner instanceof AbstractPlayer && target instanceof AbstractMonster) {
            addToBot(new DamageAction(owner, new DamageInfo(owner, trueDamage, DamageInfo.DamageType.THORNS)));
        }
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        trueDamage = info.output;
        return -1;
    }

    @Override
    public void atEndOfRound() {
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    @Override
    public void updateDescription() {
        if (newTarget != null) {
            if(newTarget instanceof AbstractMonster) {
                if (newTarget != owner) {
                    this.description = DESCRIPTIONS[0] + newTarget.name + " (monster number )";
                } else {
                    this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
                }
            }
        }
    }*/
}
