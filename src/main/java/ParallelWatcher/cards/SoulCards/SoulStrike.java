package ParallelWatcher.cards.SoulCards;


import ParallelWatcher.character.ParallelWatcher;
import ParallelWatcher.util.CardStats;
import ParallelWatcher.vfx.SmallLaserEffectColored;
import ParallelWatcher.cards.BaseSpiritorCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SoulStrike extends BaseSpiritorCard {
    public static final String ID = makeID("SoulStrike");
    private static final int DMG = 4;
    private static final int UPG_DMG = 3;
    private static final CardStats info = new CardStats(
            ParallelWatcher.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    public SoulStrike() {
        super(ID, info);
        setDamage(DMG);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (m != null) {
            this.addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.3F));
            this.addToBot(new VFXAction(new SmallLaserEffectColored(m.hb.cX, m.hb.cY, this.hb.cX, this.hb.cY, Color.WHITE), 0.12F));
        }
        this.addToBot(new DamageAction(m, new DamageInfo(AbstractDungeon.player, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DMG);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SoulStrike();
    }
}
