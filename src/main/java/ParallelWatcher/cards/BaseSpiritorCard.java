package ParallelWatcher.cards;

import ParallelWatcher.ParallelWatcherMod;
import ParallelWatcher.cardmods.SelfChannelMod;
import ParallelWatcher.util.CardStats;
import ParallelWatcher.util.GeneralUtils;
import ParallelWatcher.util.TextureLoader;
import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;


public abstract class BaseSpiritorCard extends CustomCard {
    final private static Map<String, DynamicVariable> customVars = new HashMap<>();

    protected static String makeID(String name) {
        return ParallelWatcherMod.makeID(name);
    }

    protected CardStrings cardStrings;

    final protected Map<String, LocalVarInfo> cardVariables = new HashMap<>();

    public BaseSpiritorCard(String ID, CardStats info) {
        this(ID, info.baseCost, info.cardType, info.cardTarget, info.cardRarity, info.cardColor);
    }

    public BaseSpiritorCard(String ID, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color) {
        super(ID, getName(ID), TextureLoader.getCardTextureString(GeneralUtils.removePrefix(ID), cardType), cost, getInitialDescription(ID), cardType, color, rarity, target);
        this.cardStrings = CardCrawlGame.languagePack.getCardStrings(cardID);
        this.originalName = cardStrings.NAME;
        this.belongedOrb = null;

        //System.out.println("Card Description of " + this.getClass().getName() + " : " + this.rawDescription);
    }

    private static String getName(String ID) {
        return CardCrawlGame.languagePack.getCardStrings(ID).NAME;
    }

    private static String getInitialDescription(String ID) {
        return CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION;
    }

    //Methods meant for constructor use

    protected final void setDamage(int damage) {
        this.baseDamage = this.damage = damage;
    }

    protected final void setBlock(int block) {
        this.baseBlock = this.block = block;
    }

    protected final void setMagic(int magic) {
        this.baseMagicNumber = this.magicNumber = magic;
    }


    protected final void setCustomVar(String key, int base) {
        this.setCustomVar(key, base, 0);
    }

    protected final void setCustomVar(String key, int base, int upgrade) {
        setCustomVarValue(key, base, upgrade);

        if (!customVars.containsKey(key)) {
            QuickDynamicVariable var = new QuickDynamicVariable(key);
            customVars.put(key, var);
            BaseMod.addDynamicVariable(var);
            initializeDescription();
        }
    }


    protected enum VariableType {
        DAMAGE,
        BLOCK,
        MAGIC
    }

    protected final void setCustomVar(String key, VariableType type, int base) {
        setCustomVar(key, type, base, 0);
    }

    protected final void setCustomVar(String key, VariableType type, int base, int upgrade) {
        setCustomVarValue(key, base, upgrade);

        switch (type) {
            case DAMAGE:
                calculateVarAsDamage(key);
                break;
            case BLOCK:
                calculateVarAsBlock(key);
                break;
        }

        if (!customVars.containsKey(key)) {
            QuickDynamicVariable var = new QuickDynamicVariable(key);
            customVars.put(key, var);
            BaseMod.addDynamicVariable(var);
            initializeDescription();
        }
    }

    protected final void setCustomVar(String key, VariableType type, int base, BiFunction<AbstractMonster, Integer, Integer> preCalc) {
        setCustomVar(key, type, base, 0, preCalc);
    }

    protected final void setCustomVar(String key, VariableType type, int base, int upgrade, BiFunction<AbstractMonster, Integer, Integer> preCalc) {
        setCustomVar(key, type, base, upgrade, preCalc, (m, val) -> val);
    }

    protected final void setCustomVar(String key, VariableType type, int base, BiFunction<AbstractMonster, Integer, Integer> preCalc, BiFunction<AbstractMonster, Integer, Integer> postCalc) {
        setCustomVar(key, type, base, 0, preCalc, postCalc);
    }

    protected final void setCustomVar(String key, VariableType type, int base, int upgrade, BiFunction<AbstractMonster, Integer, Integer> preCalc, BiFunction<AbstractMonster, Integer, Integer> postCalc) {
        setCustomVarValue(key, base, upgrade);

        switch (type) {
            case DAMAGE:
                setVarCalculation(key, (m, baseVal) -> {
                    boolean wasMultiDamage = this.isMultiDamage;
                    this.isMultiDamage = false;

                    int tmp = this.baseDamage;

                    this.baseDamage = baseVal;

                    this.baseDamage = preCalc.apply(m, this.baseDamage);

                    if (m != null)
                        super.calculateCardDamage(m);
                    else
                        super.applyPowers();

                    this.damage = postCalc.apply(m, this.damage);

                    this.baseDamage = tmp;
                    this.isMultiDamage = wasMultiDamage;

                    return damage;
                });
                break;
            case BLOCK:
                setVarCalculation(key, (m, baseVal) -> {
                    int tmp = this.baseBlock;

                    this.baseBlock = baseVal;

                    this.baseBlock = preCalc.apply(m, this.baseBlock);

                    if (m != null)
                        super.calculateCardDamage(m);
                    else
                        super.applyPowers();

                    this.block = postCalc.apply(m, this.block);

                    this.baseBlock = tmp;
                    return block;
                });
                break;
            default:
                setVarCalculation(key, (m, baseVal) -> {
                    int tmp = baseVal;

                    tmp = preCalc.apply(m, tmp);
                    tmp = postCalc.apply(m, tmp);

                    return tmp;
                });
                break;
        }

        if (!customVars.containsKey(key)) {
            QuickDynamicVariable var = new QuickDynamicVariable(key);
            customVars.put(key, var);
            BaseMod.addDynamicVariable(var);
            initializeDescription();
        }
    }

    private void setCustomVarValue(String key, int base, int upg) {
        cardVariables.compute(key, (k, old) -> {
            if (old == null) {
                return new LocalVarInfo(base, upg);
            } else {
                old.base = base;
                old.upgrade = upg;
                return old;
            }
        });
    }

    protected final void colorCustomVar(String key, Color normalColor) {
        colorCustomVar(key, normalColor, Settings.GREEN_TEXT_COLOR, Settings.RED_TEXT_COLOR, Settings.GREEN_TEXT_COLOR);
    }

    protected final void colorCustomVar(String key, Color normalColor, Color increasedColor, Color decreasedColor) {
        colorCustomVar(key, normalColor, increasedColor, decreasedColor, increasedColor);
    }

    protected final void colorCustomVar(String key, Color normalColor, Color increasedColor, Color decreasedColor, Color upgradedColor) {
        LocalVarInfo var = getCustomVar(key);
        if (var == null) {
            throw new IllegalArgumentException("Attempted to set color of variable that hasn't been registered.");
        }

        var.normalColor = normalColor;
        var.increasedColor = increasedColor;
        var.decreasedColor = decreasedColor;
        var.upgradedColor = upgradedColor;
    }


    private LocalVarInfo getCustomVar(String key) {
        return cardVariables.get(key);
    }

    protected void calculateVarAsDamage(String key) {
        setVarCalculation(key, (m, base) -> {
            boolean wasMultiDamage = this.isMultiDamage;
            this.isMultiDamage = false;

            int tmp = this.baseDamage;

            this.baseDamage = base;
            if (m != null)
                super.calculateCardDamage(m);
            else
                super.applyPowers();

            this.baseDamage = tmp;
            this.isMultiDamage = wasMultiDamage;

            return damage;
        });
    }

    protected void calculateVarAsBlock(String key) {
        setVarCalculation(key, (m, base) -> {
            int tmp = this.baseBlock;

            this.baseBlock = base;
            if (m != null)
                super.calculateCardDamage(m);
            else
                super.applyPowers();

            this.baseBlock = tmp;
            return block;
        });
    }

    protected void setVarCalculation(String key, BiFunction<AbstractMonster, Integer, Integer> calculation) {
        cardVariables.get(key).calculation = calculation;
    }

    public int customVarBase(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return -1;
        return var.base;
    }

    public int customVar(String key) {
        LocalVarInfo var = cardVariables == null ? null : cardVariables.get(key); //Prevents crashing when used with dynamic text
        if (var == null)
            return -1;
        return var.value;
    }

    public int[] customVarMulti(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return null;
        return var.aoeValue;
    }

    public boolean isCustomVarModified(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return false;
        return var.isModified();
    }

    public boolean customVarUpgraded(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return false;
        return var.upgraded;
    }

    protected final void setExhaust(boolean exhaust) {
        this.setExhaust(exhaust, exhaust);
    }

    protected final void setEthereal(boolean ethereal) {
        this.setEthereal(ethereal, ethereal);
    }

    protected final void setInnate(boolean innate) {
        this.setInnate(innate, innate);
    }

    protected final void setSelfRetain(boolean retain) {
        this.setSelfRetain(retain, retain);
    }

    protected final void setExhaust(boolean baseExhaust, boolean upgExhaust) {
        this.exhaust = baseExhaust;
    }

    protected final void setEthereal(boolean baseEthereal, boolean upgEthereal) {
        this.isEthereal = baseEthereal;
    }

    protected void setInnate(boolean baseInnate, boolean upgInnate) {
        this.isInnate = baseInnate;
    }

    protected void setSelfRetain(boolean baseRetain, boolean upgRetain) {
        this.selfRetain = baseRetain;
    }


    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();

        if (card instanceof BaseSpiritorCard) {
            card.rawDescription = this.rawDescription;

            ((BaseSpiritorCard) card).elapse = this.elapse;
            ((BaseSpiritorCard) card).elapseTime = this.elapseTime;
            ((BaseSpiritorCard) card).baseElapse = this.baseElapse;
            ((BaseSpiritorCard) card).baseElapseTime = this.baseElapseTime;

            for (Map.Entry<String, LocalVarInfo> varEntry : cardVariables.entrySet()) {
                LocalVarInfo target = ((BaseSpiritorCard) card).getCustomVar(varEntry.getKey()),
                        current = varEntry.getValue();
                if (target == null) {
                    ((BaseSpiritorCard) card).setCustomVar(varEntry.getKey(), current.base, current.upgrade);
                    target = ((BaseSpiritorCard) card).getCustomVar(varEntry.getKey());
                }
                target.base = current.base;
                target.value = current.value;
                target.aoeValue = current.aoeValue;
                target.upgrade = current.upgrade;
                target.calculation = current.calculation;
            }
        }

        return card;
    }

    boolean inCalc = false;

    @Override
    public void applyPowers() {
        if (!inCalc) {
            inCalc = true;
            for (LocalVarInfo var : cardVariables.values()) {
                var.value = var.calculation.apply(null, var.base);
            }
            if (isMultiDamage) {
                ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
                AbstractMonster m;
                for (LocalVarInfo var : cardVariables.values()) {
                    if (var.aoeValue == null || var.aoeValue.length != monsters.size())
                        var.aoeValue = new int[monsters.size()];

                    for (int i = 0; i < monsters.size(); ++i) {
                        m = monsters.get(i);
                        var.aoeValue[i] = var.calculation.apply(m, var.base);
                    }
                }
            }
            inCalc = false;
        }

        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        if (!inCalc) {
            inCalc = true;
            for (LocalVarInfo var : cardVariables.values()) {
                var.value = var.calculation.apply(m, var.base);
            }
            if (isMultiDamage) {
                ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
                for (LocalVarInfo var : cardVariables.values()) {
                    if (var.aoeValue == null || var.aoeValue.length != monsters.size())
                        var.aoeValue = new int[monsters.size()];

                    for (int i = 0; i < monsters.size(); ++i) {
                        m = monsters.get(i);
                        var.aoeValue[i] = var.calculation.apply(m, var.base);
                    }
                }
            }
            inCalc = false;
        }

        super.calculateCardDamage(m);
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();

        for (LocalVarInfo var : cardVariables.values()) {
            var.value = var.base;
        }
    }

    private static class QuickDynamicVariable extends DynamicVariable {
        final String localKey, key;

        private BaseSpiritorCard current = null;

        public QuickDynamicVariable(String key) {
            this.localKey = key;
            this.key = makeID(key);
        }

        @Override
        public String key() {
            return key;
        }

        @Override
        public void setIsModified(AbstractCard c, boolean v) {
            if (c instanceof BaseSpiritorCard) {
                LocalVarInfo var = ((BaseSpiritorCard) c).getCustomVar(localKey);
                if (var != null)
                    var.forceModified = v;
            }
        }

        @Override
        public boolean isModified(AbstractCard c) {
            return c instanceof BaseSpiritorCard && (current = (BaseSpiritorCard) c).isCustomVarModified(localKey);
        }

        @Override
        public int value(AbstractCard c) {
            return c instanceof BaseSpiritorCard ? ((BaseSpiritorCard) c).customVar(localKey) : 0;
        }

        @Override
        public int baseValue(AbstractCard c) {
            return c instanceof BaseSpiritorCard ? ((BaseSpiritorCard) c).customVarBase(localKey) : 0;
        }

        @Override
        public boolean upgraded(AbstractCard c) {
            return c instanceof BaseSpiritorCard && ((BaseSpiritorCard) c).customVarUpgraded(localKey);
        }

        public Color getNormalColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.CREAM_COLOR;

            return var.normalColor;
        }

        public Color getUpgradedColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.GREEN_TEXT_COLOR;

            return var.upgradedColor;
        }

        public Color getIncreasedValueColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.GREEN_TEXT_COLOR;

            return var.increasedColor;
        }

        public Color getDecreasedValueColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.RED_TEXT_COLOR;

            return var.decreasedColor;
        }
    }


    protected static class LocalVarInfo {
        int base, value, upgrade;
        int[] aoeValue = null;
        boolean upgraded = false;
        boolean forceModified = false;
        Color normalColor = Settings.CREAM_COLOR;
        Color upgradedColor = Settings.GREEN_TEXT_COLOR;
        Color increasedColor = Settings.GREEN_TEXT_COLOR;
        Color decreasedColor = Settings.RED_TEXT_COLOR;

        BiFunction<AbstractMonster, Integer, Integer> calculation = LocalVarInfo::noCalc;

        public LocalVarInfo(int base, int upgrade) {
            this.base = this.value = base;
            this.upgrade = upgrade;
        }

        private static int noCalc(AbstractMonster m, int base) {
            return base;
        }

        public boolean isModified() {
            return forceModified || base != value;
        }
    }
    //-------------------------------Spirit Orb part------------------------------------

    public boolean elapse = false;
    public boolean baseElapse = false;
    public int elapseTime = -1;
    public int baseElapseTime = -1;

    public AbstractOrb belongedOrb;

    public void setElapse(boolean elapse) {
        this.elapse = this.baseElapse = elapse;
    }

    public void setElapseTime(int elapseTime) {
        if (elapseTime >= 0) {
            setElapse(true);
            this.baseElapseTime = this.elapseTime = elapseTime;
        } else {
            setElapse(false);
            this.baseElapseTime = this.elapseTime = -1;
        }
    }

    public void doElapse(int time) {
        elapseTime -= time;
        if (elapseTime < 0) elapseTime = 0;
    }

    public void resetElapse() {
        setElapseTime(this.baseElapseTime);
        setElapse(this.baseElapse);
    }

    public void onEvoke() {
    }

    public void onElapse(AbstractMonster m) {
    }

    public void onOrbActivate() {
    }

    public void onOrbStartOfTurn() {
    }

    public boolean useWhenOrbActivated = true;

    public void setOrbUse(boolean use) {
        useWhenOrbActivated = use;
    }

    public void upgradeDescription() {
        this.rawDescription = this.cardStrings.UPGRADE_DESCRIPTION;
        if (this.rawDescription == null) {
            ParallelWatcherMod.logger.info("Card " + this.cardID + " does not have upgrade description!");
        }
        this.initializeDescription();
    }

    public void thisChannels() {
        CardModifierManager.addModifier(this, new SelfChannelMod());
    }

    //todo maybe return when exhausted
    public void thisReturns() {
        this.tags.add(ParallelWatcherMod.RETURN);
    }
}
