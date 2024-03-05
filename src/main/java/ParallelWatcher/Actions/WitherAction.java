package ParallelWatcher.Actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.TriggerMarksAction;
import com.megacrit.cardcrawl.cards.purple.PressurePoints;

public class WitherAction extends AbstractGameAction {

    private final AbstractGameAction MarkAction;

    public WitherAction() {
        MarkAction = new TriggerMarksAction(new PressurePoints());
    }

    @Override
    public void update() {
        MarkAction.update();
        this.isDone = true;
    }
}
