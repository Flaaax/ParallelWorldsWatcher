package ParallelWatcher.cards;

import ParallelWatcher.character.ParallelWatcher;
import ParallelWatcher.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;

public class CorruptedCard extends BaseSpiritorCard {

    public static final String ID = makeID("CorruptedCard");
    private static final CardStats info = new CardStats(
            ParallelWatcher.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    private static boolean canMultiUpdate;

    public static Random rng = new Random(new java.util.Random().nextLong());

    private ArrayList<Runnable> interfaceList;

    private int interfaceCount;

    public CorruptedCard() {
        super(ID, info);
        //rng = AbstractDungeon.cardRandomRng;
        rerollStat();
    }

    public void rerollStat() {

        if (AbstractDungeon.cardRandomRng != null) {
            rng = AbstractDungeon.cardRandomRng;
        }

        this.interfaceCount = rng.random(4, 6);
        //this.canMultiUpdate = getBool(0.3F);

        this.chargeCost = rng.random(-1, 4);
        //this.isCostModified = false;
        //this.isCostModifiedForTurn = false;
        this.retain = getBool(0.25F);
        this.selfRetain = retain;
        //this.dontTriggerOnUseCard = false;
        this.isInnate = getBool(0.2F);
        //this.isLocked = false;
        //this.showEvokeValue = false;
        //this.showEvokeOrbCount = 0;
        //this.upgraded = false;
        this.timesUpgraded = rng.random(0, 10);
        //this.misc = 0;
        this.ignoreEnergyOnUse = getBool(0.1F);
        //this.isSeen = true;
        //this.upgradedCost = false;
        //this.upgradedDamage = false;
        //this.upgradedBlock = false;
        //this.upgradedMagicNumber = false;
        //this.isSelected = false;
        this.exhaust = getBool(0.2F);
        this.returnToHand = getBool(0.15F);
        this.shuffleBackIntoDrawPile = getBool(0.15F);
        this.isEthereal = getBool(0.15F);
        //this.tags = new ArrayList();
        //this.isMultiDamage = false;
        this.baseDamage = rng.random(-1, 10);
        this.baseBlock = rng.random(-1, 10);
        this.baseMagicNumber = rng.random(-1, 10);
        this.baseHeal = rng.random(-1, 10);
        this.baseDraw = rng.random(-1, 10);
        this.baseDiscard = rng.random(-1, 10);
        this.damage = rng.random(-1, 10);
        this.block = rng.random(-1, 10);
        this.magicNumber = rng.random(-1, 10);
        this.heal = rng.random(-1, 10);
        this.draw = rng.random(-1, 10);
        this.discard = rng.random(-1, 10);
        //this.isDamageModified = false;
        //this.isBlockModified = false;
        //this.isMagicNumberModified = false;
        int r = rng.random(0, 4);
        if (r == 0) {
            this.target = CardTarget.ENEMY;
        } else if (r == 1) {
            this.target = CardTarget.ALL_ENEMY;
        } else if (r == 2) {
            this.target = CardTarget.SELF;
        } else if (r == 3) {
            this.target = CardTarget.NONE;
        } else if (r == 4) {
            this.target = CardTarget.ALL;
        }

        this.purgeOnUse = getBool(0.05F);
        //this.exhaustOnUseOnce = false;
        //this.exhaustOnFire = false;
        this.freeToPlayOnce = getBool(0.1F);
        //this.isInAutoplay = false;
        //this.fadingOut = false;
        //this.transparency = 1.0F;
        //this.targetTransparency = 1.0F;
        //this.goldColor = Settings.GOLD_COLOR.cpy();
        //this.renderColor = Color.WHITE.cpy();
        //this.textColor = Settings.CREAM_COLOR.cpy();
        //this.typeColor = new Color(0.35F, 0.35F, 0.35F, 0.0F);
        //this.targetAngle = 0.0F;
        //this.angle = 0.0F;
        //this.glowList = new ArrayList();
        //this.glowTimer = 0.0F;
        //this.isGlowing = false;
        //this.portraitImg = null;
        //this.useSmallTitleFont = false;
        //this.drawScale = 0.7F;
        //this.targetDrawScale = 0.7F;
        //this.isFlipped = false;
        //this.darken = false;
        //this.darkTimer = 0.0F;
        //this.hb = new Hitbox(IMG_WIDTH_S, IMG_HEIGHT_S);
        //this.hoverTimer = 0.0F;
        //this.renderTip = false;
        //this.hovered = false;
        //this.hoverDuration = 0.0F;
        //this.cardsToPreview = null;
        //this.newGlowTimer = 0.0F;
        /*this.description = new ArrayList();
        this.inBottleFlame = false;
        this.inBottleLightning = false;
        this.inBottleTornado = false;
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();*/
        this.originalName = name;
        this.name = name;
       /* this.cardID = id;
        this.assetUrl = imgUrl;
        this.portrait = cardAtlas.findRegion(imgUrl);
        this.jokePortrait = oldCardAtlas.findRegion(imgUrl);
        if (this.portrait == null) {
            if (this.jokePortrait != null) {
                this.portrait = this.jokePortrait;
            } else {
                this.portrait = cardAtlas.findRegion("status/beta");
            }
        }*/

        this.cost = rng.random(-1, 4);
        this.costForTurn = rng.random(-1, 4);
        //this.rawDescription = rawDescription;

        //this.type = getRamdomType();

        //this.color = CardLibrary.getAnyColorCard(getRandomRarity()).color;

        //this.rarity = getRandomRarity();
        //this.target = target;
        //this.damageType = dType;
        //this.damageTypeForTurn = dType;
        //this.createCardImage();
        if (name == null || rawDescription == null) {
            //logger.info("Card initialized incorrectly");
        }

        //this.initializeTitle();
        //this.initializeDescription();
        //this.updateTransparency();
        //this.uuid = UUID.randomUUID();
    }

    public boolean getBool(float prob) {
        return rng.random() < prob;
    }

    public CardRarity getRandomRarity() {
        int roll = rng.random(0, 99);
        AbstractCard.CardRarity cardRarity;
        if (roll < 30) {
            cardRarity = CardRarity.COMMON;
        } else if (roll < 65) {
            cardRarity = CardRarity.UNCOMMON;
        } else if (roll < 93) {
            cardRarity = CardRarity.RARE;
        } else if (roll < 98) {
            cardRarity = CardRarity.CURSE;
        } else {
            cardRarity = CardRarity.BASIC;
        }
        return cardRarity;
    }

    public CardType getRamdomType() {
        int roll = rng.random(0, 4);
        CardType type;
        if (roll == 0) {
            type = CardType.ATTACK;
        } else if (roll == 1) {
            type = CardType.SKILL;
        } else if (roll == 2) {
            type = CardType.CURSE;
        } else if (roll == 3) {
            type = CardType.POWER;
        } else {
            type = CardType.STATUS;
        }
        return type;
    }

    @Override
    public void upgrade() {
        if (canMultiUpdate || !upgraded) {
            upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new GainBlockAction(p, p, this.block));
    }
}
