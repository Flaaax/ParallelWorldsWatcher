package ParallelWatcher.relics;

import ParallelWatcher.Actions.PlaceActualCardIntoOrb;
import ParallelWatcher.character.ParallelWatcher;
import ParallelWatcher.cards.SoulCall;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static ParallelWatcher.ParallelWatcherMod.makeID;

public class SoulCore extends BaseRelic {
    private static final String NAME = "SoulCore"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.STARTER; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.

    public SoulCore() {
        super(ID, NAME, ParallelWatcher.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new IncreaseMaxOrbAction(2));
        this.addToBot(new PlaceActualCardIntoOrb(SoulCall.getRandomCard(AbstractDungeon.cardRandomRng)));
        //this.addToBot(new PlaceActualCardIntoOrb(new Omega_Spiritor()));
    }

    @Override
    public void atTurnStart() {
        //this.addToTop(new PlaceActualCardIntoOrb(new Defend_Spiritor()));   //test
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulCore();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
