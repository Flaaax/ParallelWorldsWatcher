package ParallelWatcher.Actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

//using this class might cause dangerous
public class ExecuteFuncAction extends AbstractGameAction {

    private final Runnable function;

    // 构造函数接受一个Runnable类型的函数
    public ExecuteFuncAction(Runnable function) {
        this.function = function;
    }

    @Override
    public void update() {
        if (function != null) {
            function.run();
        }
        this.isDone = true;
    }
}
