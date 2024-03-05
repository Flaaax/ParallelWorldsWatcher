package ParallelWatcher.relics;

import ParallelWatcher.character.ParallelWatcher;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EchoPower;
import com.megacrit.cardcrawl.powers.MayhemPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static ParallelWatcher.ParallelWatcherMod.makeID;

public class AutoCard extends BaseRelic {
    private static final String NAME = "AutoCard"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.MAGICAL; //The sound played when the relic is clicked.

    public AutoCard() {
        super(ID, NAME, ParallelWatcher.Enums.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        //在战斗开始时触发
        flash();
        //AbstractCreature owner = AbstractDungeon.getRandomMonster();
        //this.addToTop(new ApplyPowerAction(owner, null, new ChannelingPower(owner, 7)));
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MayhemPower(AbstractDungeon.player, 1)));
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new EchoPower(AbstractDungeon.player,1)));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AutoCard();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
