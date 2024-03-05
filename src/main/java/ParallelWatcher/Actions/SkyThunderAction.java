package ParallelWatcher.Actions;

import ParallelWatcher.powers.ChannelingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@Deprecated
public class SkyThunderAction extends AbstractGameAction {
    private final DamageInfo info;

    public SkyThunderAction(AbstractCreature target,DamageInfo info){
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;
    }

    @Override
    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else {
            this.tickDuration();
            if (this.isDone) {
                this.target.damage(this.info);
                if (this.target.lastDamageTaken > 0) {
                    this.addToTop(new ApplyPowerAction(target,source,new ChannelingPower(target, target.lastDamageTaken)));
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                } else {
                    //this.addToTop(new WaitAction(0.1F));
                }
            }

        }
    }
}
