package ParallelWatcher.cardmods;

import ParallelWatcher.ParallelWatcherMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SelfChannelMod extends AbstractCardModifier {

    public static String ID = ParallelWatcherMod.makeID("SelfChannelModifier");

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.hasTag(ParallelWatcherMod.SELF_CHANNEL)) {
            card.tags.add(ParallelWatcherMod.SELF_CHANNEL);
        }
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SelfChannelMod();
    }
}
